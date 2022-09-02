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

    public static void makeConnection() throws SQLException {
        connection = DriverManager.getConnection(dbURL, userName, password);
    }

    public static void closeConnection() throws SQLException {
        connection.close();
    }


    public static Connection getConnection() {
        return connection;
    }

}
