package dev.sam.scheduler.dao;

import dev.sam.scheduler.model.Appointment;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AppointmentDAO {
    int getNumberOfAppointmentsFromCustomerId(int customerId) throws SQLException;
}
