package dev.sam.scheduler.dao;

import dev.sam.scheduler.database.DB;
import dev.sam.scheduler.helper.DateAndTimeHelper;
import dev.sam.scheduler.helper.SQLHelper;
import dev.sam.scheduler.helper.StringHelper;
import dev.sam.scheduler.model.Appointment;
import dev.sam.scheduler.model.SharedData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;
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

    public int getMaxAppointmentId() throws SQLException {
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(Appointment_ID) FROM appointments");
        int maxId = 0;
        while (rs.next()) {
            maxId = rs.getInt(1);
        }
        return maxId;
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

    @Override
    public void insert(Appointment appointment) throws SQLException {
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        String sqlStatement = (
                "INSERT INTO appointments " +
                        "VALUES" +
                        "(" +
                        appointment.getId() + "," +
                        StringHelper.toStatementItem(appointment.getTitle()) +
                        StringHelper.toStatementItem(appointment.getDescription()) +
                        StringHelper.toStatementItem(appointment.getLocation()) +
                        StringHelper.toStatementItem(appointment.getType()) +
                        StringHelper.toStatementItem(DateAndTimeHelper.offsetDateTimeToDbStr(appointment.getStartDateTime())) +
                        StringHelper.toStatementItem(DateAndTimeHelper.offsetDateTimeToDbStr(appointment.getEndDateTime())) +
                        StringHelper.toStatementItem(DateAndTimeHelper.offsetDateTimeToDbStr(appointment.getCreationDate())) +
                        StringHelper.toStatementItem(appointment.getCreatedBy()) +
                        StringHelper.toStatementItem(DateAndTimeHelper.offsetDateTimeToDbStr(appointment.getLastUpdatedDate())) +
                        StringHelper.toStatementItem(appointment.getLastUpdatedBy()) +
                        appointment.getCustomerId() + "," +
                        appointment.getUserId() + "," +
                        appointment.getContactId() +
                        ")");
        System.out.println(sqlStatement);
        stmt.executeUpdate(sqlStatement);
        DB.closeConnection();
    }

    @Override
    public void update(Appointment appointment) throws SQLException {
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        String sqlStatement = SQLHelper.updateStatement(
                "appointments",
                "WHERE Appointment_ID = " + appointment.getId(),
                SQLHelper.makeSetString("Title", appointment.getTitle()),
                SQLHelper.makeSetString("Description", appointment.getDescription()),
                SQLHelper.makeSetString("Location", appointment.getLocation()),
                SQLHelper.makeSetString("Type", appointment.getType()),
                SQLHelper.makeSetString("Start", DateAndTimeHelper.offsetDateTimeToDbStr(appointment.getStartDateTime())),
                SQLHelper.makeSetString("End", DateAndTimeHelper.offsetDateTimeToDbStr(appointment.getEndDateTime())),
                SQLHelper.makeSetString("Last_Update", DateAndTimeHelper.offsetDateTimeToDbStr(appointment.getLastUpdatedDate())),
                SQLHelper.makeSetString("Last_Updated_By", appointment.getLastUpdatedBy()),
                SQLHelper.makeSetString("Customer_ID", appointment.getCustomerId()),
                SQLHelper.makeSetString("User_ID", appointment.getUserId()),
                SQLHelper.makeSetString("Contact_ID", appointment.getContactId())
        );
        System.out.println(sqlStatement);
        stmt.executeUpdate(sqlStatement);
        DB.closeConnection();
    }

    @Override
    public void delete(Appointment appointment) throws SQLException {

    }
}
