package dev.sam.scheduler.database;

import dev.sam.scheduler.model.Customer;
import dev.sam.scheduler.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final String dbName = "client_schedule";
    private static final String dbURL = "jdbc:mysql://localhost:3306/" + dbName;
    private static final String userName = "root";
    private static final String password = "password";
    private static Connection connection;
    private static User activeUser;
    private static Customer activeCustomer;

    public static Customer getActiveCustomer() {
        return activeCustomer;
    }

    public static void setActiveCustomer(Customer activeCustomer) {
        DB.activeCustomer = activeCustomer;
    }

    public static void makeConnection() throws SQLException {
        connection = DriverManager.getConnection(dbURL, userName, password);
    }

    public static void closeConnection() throws SQLException {
        connection.close();
    }

    public static User getActiveUser() {
        return DB.activeUser;
    }

    public static void setActiveUser(User user) {
        DB.activeUser = user;
    }

    public static Connection getConnection() {
        return connection;
    }

}
