package dev.sam.scheduler.model;

import dev.sam.scheduler.controller.CustomerFormController;
import dev.sam.scheduler.controller.CustomerTableController;

public enum SharedData {
INSTANCE;

    public CustomerTableController getCustomerTableController() {
        return customerTableController;
    }

    public void setCustomerTableController(CustomerTableController customerTableController) {
        this.customerTableController = customerTableController;
    }

    private CustomerTableController customerTableController;
}
