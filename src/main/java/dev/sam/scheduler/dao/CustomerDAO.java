package dev.sam.scheduler.dao;

import dev.sam.scheduler.model.Customer;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface CustomerDAO {
    ObservableList<Customer> getAllCustomers() throws SQLException;
    void insertCustomer(Customer customer) throws SQLException;

    int getMaxCustomerId() throws SQLException;
}
