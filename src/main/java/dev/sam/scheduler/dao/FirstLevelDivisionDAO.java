package dev.sam.scheduler.dao;

import dev.sam.scheduler.model.Country;
import dev.sam.scheduler.model.FirstLevelDivision;

import java.sql.SQLException;
import java.util.ArrayList;

public interface FirstLevelDivisionDAO {
    ArrayList<FirstLevelDivision> getAllDivisions() throws SQLException;
}
