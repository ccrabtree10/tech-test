package com.crabware.techtest.databasecli.database;

import java.sql.SQLException;

public class FoodMartHelper {
    private final Database database;
    private static final String EMPLOYEES_QUERY = "SELECT * " +
            "FROM foodmart.employee e " +
            "    INNER JOIN foodmart.department d " +
            "        on d.department_id = e.department_id " +
            "    INNER JOIN foodmart.position p " +
            "        on p.position_id = e.position_id " +
            "WHERE " +
            "    d.department_id = ? " +
            "    AND p.pay_type = ? " +
            "    AND e.education_level = ? ";

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
