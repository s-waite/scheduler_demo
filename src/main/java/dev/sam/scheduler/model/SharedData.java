package dev.sam.scheduler.model;

import dev.sam.scheduler.controller.AppointmentTableController;
import dev.sam.scheduler.controller.CustomerTableController;
import dev.sam.scheduler.dao.CountryDAO;
import dev.sam.scheduler.dao.FirstLevelDivisionDAO;
import javafx.scene.control.TabPane;

import java.sql.SQLException;
import java.util.ArrayList;

public enum SharedData {
    INSTANCE;

    SharedData() {
        try {
            firstLevelDivisions = new FirstLevelDivisionDAO().getAll();
            countries = new CountryDAO().getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User activeUser;
    private Customer activeCustomer;
    private Appointment activeAppointment;
    private CustomerTableController customerTableController;
    private AppointmentTableController appointmentTableController;
    private final ArrayList<FirstLevelDivision> firstLevelDivisions;
    private TabPane mainTabPane;

    public Appointment getActiveAppointment() {
        return activeAppointment;
    }

    public void setActiveAppointment(Appointment activeAppointment) {
        this.activeAppointment = activeAppointment;
    }

    public ArrayList<FirstLevelDivision> getFirstLevelDivisions() {
        return firstLevelDivisions;
    }

    public AppointmentTableController getAppointmentTableController() {
        return appointmentTableController;
    }

    public void setAppointmentTableController(AppointmentTableController appointmentTableController) {
        this.appointmentTableController = appointmentTableController;
    }

    public void setMainTabPane(TabPane tabPane) {
        this.mainTabPane = tabPane;
    }

    public TabPane getMainTabPane() {
        return this.mainTabPane;
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    private final ArrayList<Country> countries;

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

    //TODO: add static db data
}
