package com.crabware.techtest.databasecli.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FoodMartHelper {
    private final Database database;
    private static final String EMPLOYEES_QUERY = "select * from foodmart.employee";

    private FoodMartHelper(Database database) {
        this.database = database;
    }

    public QueryResult getEmployees(String department, String payType, String educationLevel) throws SQLException {
        String[] params = new String[]{department, payType, educationLevel};
        return database.executeStatement(EMPLOYEES_QUERY, params);
    }

    public static FoodMartHelper from(Database database) {
        return new FoodMartHelper(database);
    }
}
