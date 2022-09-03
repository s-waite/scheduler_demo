package dev.sam.scheduler.dao;

import dev.sam.scheduler.database.DB;
import dev.sam.scheduler.model.Country;
import dev.sam.scheduler.model.FirstLevelDivision;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FirstLevelDivisionDAOImpl implements FirstLevelDivisionDAO {

    @Override
    public ArrayList<FirstLevelDivision> getAllDivisions() throws SQLException {
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
}
