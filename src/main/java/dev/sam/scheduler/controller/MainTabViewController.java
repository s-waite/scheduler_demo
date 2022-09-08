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

        // When the tab is switched to the customer table, set the active customer to null since there should be no
        // active customers when we are viewing the customer table
        customerTab.setOnSelectionChanged(event -> {
            // Fires when the customer tab is selected
            if (customerTab.isSelected()) {
                // If the use selects the customer tab, we know there is no active customer
                System.out.println("customer");
                SharedData.INSTANCE.setActiveCustomer(null);
            }
        });

        // If the tab is switched to appointments and there is an active customer, filter the appointment table by that customer
        appointmentTab.setOnSelectionChanged(event -> {
            if (appointmentTab.isSelected()) {
                appointmentTableController.onThisTabSelected();
            }
        });

    }
}

