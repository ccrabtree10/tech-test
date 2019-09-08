package com.crabware.techtest.databasecli.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represents a database, intended to be easier than interacting directly with JDBC classes.
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
     * Construct a database from the given parameters.
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
     * Execute a statement against the database. If the statement is parameterised, populate with the supplied
     * <code>params</code>.
     *
     * @param query  the SQL query
     * @param params the params (should be empty if query has no parameters)
     * @return the query result
     * @throws SQLException if there is an error whilst executing the query
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

    /**
     * Gets a list of employees for the given department id, pay type and education level.
     *
     * @param departmentId     the department id
     * @param payType          the pay type
     * @param educationLevel   the education level
     * @return the employees
     * @throws SQLException if an exception occurs whilst executing the statement to get employees
     */
    public QueryResult getEmployees(String departmentId, String payType, String educationLevel) throws SQLException {
        if (departmentId == null || payType == null || educationLevel == null) {
            String args = "departmentId=" + departmentId + ", payType=" + payType + ", educationLevel=" + educationLevel;
            throw new IllegalArgumentException("Arguments must not be null, supplied arguments: " + args);
        }
        String[] params = new String[]{departmentId, payType, educationLevel};
        return executeStatement(EMPLOYEES_QUERY, params);
    }


}
