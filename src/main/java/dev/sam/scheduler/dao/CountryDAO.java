package dev.sam.scheduler.dao;

import dev.sam.scheduler.model.Country;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CountryDAO {
    ArrayList<Country> getAllCountries() throws SQLException;
}
