package dev.sam.scheduler.dao;

import dev.sam.scheduler.database.DB;
import dev.sam.scheduler.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAO<User> {
    @Override
    public List<User> getAll() throws SQLException {
        ArrayList<User> allUsers = new ArrayList<>();
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
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
