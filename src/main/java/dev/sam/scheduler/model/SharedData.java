package dev.sam.scheduler.model;

import dev.sam.scheduler.controller.CustomerTableController;
import dev.sam.scheduler.database.DB;

public enum SharedData {
    INSTANCE;

    private User activeUser;
    private Customer activeCustomer;
    private CustomerTableController customerTableController;

    public CustomerTableController getCustomerTableController() {
        return customerTableController;
    }

    public void setCustomerTableController(CustomerTableController customerTableController) {
        this.customerTableController = customerTableController;
    }

    public Customer getActiveCustomer() {
        return activeCustomer;
    }

    public void setActiveCustomer(Customer activeCustomer) {
        this.activeCustomer = activeCustomer;
    }

    public User getActiveUser() {
        return this.activeUser;
    }

    public void setActiveUser(User user) {
        this.activeUser = user;
    }
}
