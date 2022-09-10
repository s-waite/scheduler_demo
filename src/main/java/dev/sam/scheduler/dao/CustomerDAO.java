package dev.sam.scheduler.dao;

import dev.sam.scheduler.database.DB;
import dev.sam.scheduler.helper.DateAndTimeHelper;
import dev.sam.scheduler.helper.SQLHelper;
import dev.sam.scheduler.helper.StringHelper;
import dev.sam.scheduler.model.Customer;
import dev.sam.scheduler.model.SharedData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;

public class CustomerDAO implements DAO<Customer> {
    public ObservableList<Customer> getAll() throws SQLException {
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

    public void insert(Customer customer) throws SQLException {
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        String sqlStatement = (
                "INSERT INTO customers " +
                        "VALUES" +
                        "(" +
                        customer.getId() + "," +
                        StringHelper.toStatementItem(customer.getName()) +
                        StringHelper.toStatementItem(customer.getAddress()) +
                        StringHelper.toStatementItem(customer.getPostalCode()) +
                        StringHelper.toStatementItem(customer.getPhone()) +
                        StringHelper.toStatementItem(DateAndTimeHelper.offsetDateTimeToDbStr(customer.getCreationDate())) +
                        StringHelper.toStatementItem(customer.getCreatedBy()) +
                        StringHelper.toStatementItem(DateAndTimeHelper.offsetDateTimeToDbStr(customer.getLastUpdatedDate())) +
                        StringHelper.toStatementItem(customer.getLastUpdatedBy()) +
                        customer.getDivisionId() +
                        ")");
        stmt.executeUpdate(sqlStatement);
        DB.closeConnection();
    }

    public void update(Customer customer) throws SQLException {
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        String sqlStatement = SQLHelper.updateStatement(
                "customers",
                "WHERE Customer_ID = " + customer.getId(),
                SQLHelper.makeSetString("Customer_Name", customer.getName()),
                SQLHelper.makeSetString("Address", customer.getAddress()),
                SQLHelper.makeSetString("Postal_Code", customer.getPostalCode()),
                SQLHelper.makeSetString("Phone", customer.getPhone()),
                SQLHelper.makeSetString("Last_Update", DateAndTimeHelper.offsetDateTimeToDbStr(customer.getLastUpdatedDate())),
                SQLHelper.makeSetString("Last_Updated_By", SharedData.INSTANCE.getActiveUser().getUserName()),
                SQLHelper.makeSetString("Division_ID", customer.getDivisionId())
        );
        System.out.println(sqlStatement);
        stmt.executeUpdate(sqlStatement);
        DB.closeConnection();
    }

    public void delete(Customer customer) throws SQLException {
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        stmt.executeUpdate("DELETE FROM customers WHERE Customer_ID = " + customer.getId());
        DB.closeConnection();

    }

    public int getMaxCustomerId() throws SQLException {
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(Customer_ID) FROM Customers");
        int maxId = 0;
        while (rs.next()) {
            maxId = rs.getInt(1);
        }
        return maxId;
    }
}
