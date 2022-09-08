package dev.sam.scheduler.dao;

import dev.sam.scheduler.database.DB;
import dev.sam.scheduler.model.Appointment;
import dev.sam.scheduler.model.Country;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AppointmentDAOImpl implements AppointmentDAO {
    @Override
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

}
