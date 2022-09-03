package dev.sam.scheduler.model;

import dev.sam.scheduler.controller.CustomerTableController;
import dev.sam.scheduler.dao.CountryDAOImpl;
import dev.sam.scheduler.dao.FirstLevelDivisionDAOImpl;

import java.sql.SQLException;
import java.util.ArrayList;

public enum SharedData {
    INSTANCE;

    SharedData() {
        try {
            firstLevelDivisions = new FirstLevelDivisionDAOImpl().getAllDivisions();
            countries = new CountryDAOImpl().getAllCountries();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User activeUser;
    private Customer activeCustomer;
    private CustomerTableController customerTableController;
    private final ArrayList<FirstLevelDivision> firstLevelDivisions;

    public ArrayList<FirstLevelDivision> getFirstLevelDivisions() {
        return firstLevelDivisions;
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
