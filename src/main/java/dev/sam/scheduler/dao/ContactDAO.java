package dev.sam.scheduler.dao;

import dev.sam.scheduler.database.DB;
import dev.sam.scheduler.model.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO implements DAO<Contact> {
    @Override
    public List<Contact> getAll() throws SQLException {
        List<Contact> allContacts = new ArrayList<>();
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM contacts");
        while (rs.next()) {
           int id = rs.getInt("Contact_ID");
           String name = rs.getString("Contact_Name");
           String email = rs.getString("Email");

           allContacts.add(new Contact(id, name, email));
        }
        return allContacts;
    }

    @Override
    public void insert(Contact contact) throws SQLException {

    }

    @Override
    public void update(Contact contact) throws SQLException {

    }

    @Override
    public void delete(Contact contact) throws SQLException {

    }

    public Contact getContactFromId(int contactId) throws SQLException {
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM contacts WHERE Contact_ID = " + contactId);
        Contact contact = null;
        while (rs.next()) {
            int id = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            String email = rs.getString("Email");
            contact = new Contact(id, contactName, email);
        }
        DB.closeConnection();
        return contact;
    }
}
