package dev.sam.scheduler.dao;

import dev.sam.scheduler.model.Customer;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerDAO {
    ObservableList<Customer> getAllCustomers() throws SQLException;
    void insertCustomer(Customer customer) throws SQLException;
}
