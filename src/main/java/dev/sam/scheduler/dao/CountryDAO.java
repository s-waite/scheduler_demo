package dev.sam.scheduler.dao;

import dev.sam.scheduler.database.DB;
import dev.sam.scheduler.model.Country;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CountryDAO implements DAO<Country> {

    @Override
    public ArrayList<Country> getAll() throws SQLException {
        ArrayList<Country> allCountries = new ArrayList<>();
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Countries");
        while (rs.next()) {
            int countryId = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");

            allCountries.add(new Country(countryId, countryName));
        }
        return allCountries;
    }

    @Override
    public void insert(Country country) throws SQLException {

    }

    @Override
    public void update(Country country) throws SQLException {

    }

    @Override
    public void delete(Country country) throws SQLException {

    }

    public String getCountryNameFromDivisionId(int divisionId) throws SQLException {
        String country = null;
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT Country FROM client_schedule.countries" +
                        " INNER JOIN first_level_divisions ON countries.Country_ID = first_level_divisions.COUNTRY_ID" +
                        " WHERE Division_ID = " + divisionId);
        while (rs.next()) {
            country = rs.getString(1);
        }
        return country;
    }
}
