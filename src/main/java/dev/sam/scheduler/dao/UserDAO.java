package dev.sam.scheduler.dao;

import dev.sam.scheduler.model.User;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UserDAO {
     ArrayList<User> getAllUsers() throws SQLException;
}
