package dev.sam.scheduler.dao;

import dev.sam.scheduler.database.DB;
import dev.sam.scheduler.helper.DateAndTimeHelper;
import dev.sam.scheduler.model.Appointment;
import dev.sam.scheduler.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO implements DAO<Appointment> {
    public int getNumberOfAppointmentsFromCustomerId(int customerId) throws SQLException {
        int numOfAppointments = 0;
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM customers" +
                " INNER JOIN appointments ON customers.Customer_ID = appointments.Customer_ID" +
                " WHERE customers.Customer_ID = " + customerId);
        while (rs.next()) {
            numOfAppointments += 1;
        }
        DB.closeConnection();
        return numOfAppointments;
    }

    @Override
    public List<Appointment> getAll() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM appointments");
        while (rs.next()) {
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            OffsetDateTime start = DateAndTimeHelper.dbDateStringToDateTime(rs.getString("Start"));
            OffsetDateTime end = DateAndTimeHelper.dbDateStringToDateTime(rs.getString("End"));
            OffsetDateTime creationDate = DateAndTimeHelper.dbDateStringToDateTime(rs.getString("Create_Date"));
            String createdBy = rs.getString("Created_By");
            OffsetDateTime lastUpdatedDate = DateAndTimeHelper.dbDateStringToDateTime(rs.getString("Last_Update"));
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");

            allAppointments.add(new Appointment(
                    appointmentId,
                    title,
                    description,
                    location,
                    type,
                    start,
                    end,
                    creationDate,
                    createdBy,
                    lastUpdatedDate,
                    lastUpdatedBy,
                    customerId,
                    userId,
                    contactId
            ));
        }
        return allAppointments;
    }

    public List<Appointment> getAppointmentsOfCustomer(Customer customer) throws SQLException {
        ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM appointments WHERE Customer_ID = " + customer.getId());
        while (rs.next()) {
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            OffsetDateTime start = DateAndTimeHelper.dbDateStringToDateTime(rs.getString("Start"));
            OffsetDateTime end = DateAndTimeHelper.dbDateStringToDateTime(rs.getString("End"));
            OffsetDateTime creationDate = DateAndTimeHelper.dbDateStringToDateTime(rs.getString("Create_Date"));
            String createdBy = rs.getString("Created_By");
            OffsetDateTime lastUpdatedDate = DateAndTimeHelper.dbDateStringToDateTime(rs.getString("Last_Update"));
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");

            customerAppointments.add(new Appointment(
                    appointmentId,
                    title,
                    description,
                    location,
                    type,
                    start,
                    end,
                    creationDate,
                    createdBy,
                    lastUpdatedDate,
                    lastUpdatedBy,
                    customerId,
                    userId,
                    contactId
            ));

        }
        DB.closeConnection();
        return customerAppointments;
    }
}
