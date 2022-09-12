package dev.sam.scheduler.controller;

import dev.sam.scheduler.dao.AppointmentDAO;
import dev.sam.scheduler.model.Appointment;
import dev.sam.scheduler.model.SharedData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainTabViewController implements Initializable {
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab customerTab;

    @FXML
    private Tab appointmentTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkForAppointments();
        SharedData.INSTANCE.setMainTabPane(tabPane);
        CustomerTableController customerTableController = SharedData.INSTANCE.getCustomerTableController();
        AppointmentTableController appointmentTableController = SharedData.INSTANCE.getAppointmentTableController();

        /**
         * This listener is called when the tab is changed.
         *
         * Calls the function in the controller that is designated to execute when the tab is changed
         */
        customerTab.setOnSelectionChanged(event -> {
            // Fires when the customer tab is selected
            if (customerTab.isSelected()) {
               customerTableController.onThisTabSelected();
            }
        });

        appointmentTab.setOnSelectionChanged(event -> {
            if (appointmentTab.isSelected()) {
                appointmentTableController.onThisTabSelected();
            }
        });

    }

    public void checkForAppointments() {
        ZonedDateTime userTime = ZonedDateTime.now(ZoneId.systemDefault());
        System.out.println(userTime);
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        List<Appointment> appointmentsSoon = new ArrayList<>();
        boolean appointmentWithin15Mins = false;
        try {
            List<Appointment> appointments = appointmentDAO.getAllOfUser(SharedData.INSTANCE.getActiveUser());
            for (Appointment appointment : appointments) {
                ZonedDateTime appointmentStart = appointment.getStartDateTime().atZoneSameInstant(ZoneId.systemDefault());
                if (userTime.isEqual(appointmentStart)) {
                    appointmentWithin15Mins = true;
                    appointmentsSoon.add(appointment);
                } else if (userTime.isBefore(appointmentStart) && userTime.plusMinutes(15).isAfter(appointmentStart)) {
                    appointmentWithin15Mins = true;
                    appointmentsSoon.add(appointment);
                } else {
                    appointmentWithin15Mins = false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Alert appointmentAlert = new Alert(Alert.AlertType.INFORMATION);
        if (appointmentWithin15Mins) {
            appointmentAlert.setHeaderText("You have appointments soon");
            StringBuilder appointmentList = new StringBuilder();
            appointmentsSoon.forEach(appointment -> {
                appointmentList
                        .append("ID: ")
                        .append(appointment.getId())
                        .append(", Time: ")
                        .append(appointment.getStartDateTime().atZoneSameInstant(ZoneId.systemDefault()))
                        .append("\n");
            });
            appointmentAlert.setContentText(appointmentList.toString());
        } else {
            appointmentAlert.setHeaderText("Upcoming appointments");
            appointmentAlert.setContentText("You have no upcoming appointments");
        }
        appointmentAlert.showAndWait();
    }
}

