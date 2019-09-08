package com.crabware.techtest.databasecli.databaseutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represents a database, intended to be easier than directly interacting with JDBC classes.
 */
public class Database {
    private final String url;
    private final String username;
    private final String password;
    private final ConnectionSource connectionSource;
    private Connection connection = null;

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

    private Database(String url, String username, String password, ConnectionSource connectionSource) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.connectionSource = connectionSource;
    }

    /**
     * From database.
     *
     * @param url              the url
     * @param username         the username
     * @param password         the password
     * @param connectionSource the connection source
     * @return the database
     */
    public static Database from(String url, String username, String password, ConnectionSource connectionSource) {
        return new Database(url, username, password, connectionSource);
    }

    /**
     * Connect to the database.
     *
     * @throws SQLException if there is an error whilst connecting
     */
    public void connect() throws SQLException {
        if (connection == null) {
            connection = connectionSource.getConnection(url, username, password);
        }
    }

    /**
     * Disconnect from the database.
     *
     * @throws SQLException if there is an error whilst disconnecting
     */
    public void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Execute statement query result.
     *
     * @param query  the query
     * @param params the params
     * @return the query result
     * @throws SQLException the sql exception
     */
    public QueryResult executeStatement(String query, String[] params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }

        QueryResult queryResult = null;
        try {
            statement.execute();
            // Build the query result before we close the statement otherwise getResultSet will throw an exception
            queryResult = QueryResult.from(statement.getResultSet());
        } finally {
            statement.close();
        }
        return queryResult;
    }

    public QueryResult getEmployees(String department, String payType, String educationLevel) throws SQLException {
        if (department == null || payType == null || educationLevel == null) {
            String args = "department=" + department + ", payType=" + payType + ", educationLevel=" + educationLevel;
            throw new IllegalArgumentException("Arguments must not be null, supplied arguments: " + args);
        }
        String[] params = new String[]{department, payType, educationLevel};
        return executeStatement(EMPLOYEES_QUERY, params);
    }


}
