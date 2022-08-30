package dev.sam.scheduler.dao;

import dev.sam.scheduler.database.DB;
import dev.sam.scheduler.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDAOImpl implements UserDAO {
    public ArrayList<User> getAllUsers() throws SQLException {
        ArrayList<User> allUsers = new ArrayList<>();
        DB.makeConnection();
        Statement stmt = DB.connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM USERS");
        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");

            allUsers.add(new User(
                    userId,
                    userName,
                    password)
            );
        }
        DB.closeConnection();
        return allUsers;
    }
}
