package dev.sam.scheduler.dao;

import dev.sam.scheduler.database.DB;
import dev.sam.scheduler.model.FirstLevelDivision;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FirstLevelDivisionDAO implements DAO<FirstLevelDivision> {
    @Override
    public ArrayList<FirstLevelDivision> getAll() throws SQLException {
        ArrayList<FirstLevelDivision> allDivisions = new ArrayList<>();
        DB.makeConnection();
        Statement stmt = DB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM first_level_divisions");
        while (rs.next()) {
            int divisionId = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            int countryId = rs.getInt("COUNTRY_ID");

            allDivisions.add(new FirstLevelDivision(
                    divisionId,
                    divisionName,
                    countryId
            ));
        }
        return allDivisions;
    }

    @Override
    public void insert(FirstLevelDivision firstLevelDivision) throws SQLException {

    }

    @Override
    public void update(FirstLevelDivision firstLevelDivision, Integer itemId) throws SQLException {

    }

    @Override
    public void delete(FirstLevelDivision firstLevelDivision) throws SQLException {

    }
}
