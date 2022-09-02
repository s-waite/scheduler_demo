package dev.sam.scheduler.helper;

import java.sql.SQLException;

public class SQLHelper {
    /**
     * Returns a complete SQL UPDATE statement string.
     *
     * @param tableName  The table to update
     * @param setStrings The set strings i.e Customer_ID = 1
     * @return
     */
    public static String updateStatement(String tableName, String whereCondition, String... setStrings) {
        StringBuilder sqlStatement = new StringBuilder("UPDATE " + tableName + " SET ");
        int numSetStrings = setStrings.length;
        int count = 0;


        for (String setString : setStrings) {
            System.out.println(count);
            System.out.println(numSetStrings);
            if (count < numSetStrings - 1) {
                sqlStatement
                        .append(setString)
                        .append(", ");
            } else {
                sqlStatement
                        .append(setString)
                        .append(" ");
            }
            count += 1;
        }
        sqlStatement.append(whereCondition);
        return sqlStatement.toString();
    }

    /**
     * Creates each individual set string for a SQL UPDATE statement.
     * <p>
     * For example if you pass in the column name "User_ID" and 1, it will return the string User_ID = 1
     * The method will add quotes to an item if it is a string
     *
     * @param columnName
     * @param itemToSet
     * @return
     * @throws SQLException
     */
    public static String makeSetString(String columnName, Object itemToSet) throws SQLException {
        if (itemToSet instanceof String) {
            return columnName + " = \"" + itemToSet + "\"";
        } else if (itemToSet instanceof Integer) {
            return columnName + " = " + itemToSet;
        } else {
            throw new SQLException("Set string can only use integers or strings");
        }
    }
}
