package dev.sam.scheduler.dao;

import dev.sam.scheduler.database.DB;
import dev.sam.scheduler.helper.DateAndTimeHelper;
import dev.sam.scheduler.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Customers");
        while (rs.next()) {
            Integer customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            String creationDateStr = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdateStr = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            Integer divisionId = rs.getInt("Division_ID");

            OffsetDateTime creationDate = DateAndTimeHelper.dbDateStringToDateTime(creationDateStr);
            OffsetDateTime lastUpdatedDate = DateAndTimeHelper.dbDateStringToDateTime(lastUpdateStr);

            allCustomers.add(new Customer(
                    customerId,
                    customerName,
                    address,
                    postalCode,
                    phone,
                    creationDate,
                    createdBy,
                    lastUpdatedDate,
                    lastUpdatedBy,
                    divisionId
            ));
        }
        DB.closeConnection();
        return allCustomers;
    }

    @Override
    public void insertCustomer(Customer customer) throws SQLException {
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        stmt.executeUpdate(
                "INSERT INTO customers" +
                        "VALUES" +
                        "(" +
                        customer.getId() + "," +
                        customer.getName() + "," +
                        customer.getAddress() + "," +
                        customer.getPostalCode() + "," +
                        customer.getPhone() + "," +
                        customer.getCreationDate() + "," +
                        customer.getCreatedBy() + "," +
                        customer.getLastUpdatedDate() + "," +
                        customer.getLastUpdatedBy() + "," +
                        customer.getDivisionId() + "," +
                        ")"
        );
        DB.closeConnection();
    }
}
