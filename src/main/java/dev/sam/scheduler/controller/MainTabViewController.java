package dev.sam.scheduler.controller;

import dev.sam.scheduler.model.SharedData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
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
}

