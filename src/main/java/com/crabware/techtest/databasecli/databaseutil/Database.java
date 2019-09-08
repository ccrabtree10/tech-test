package com.crabware.techtest.databasecli.databaseutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represents a database, intended to be easier to use than directly interacting with JDBC classes.
 */
public class Database {
    private final String url;
    private final String username;
    private final String password;
    private final ConnectionSource connectionSource;
    private Connection connection = null;

    private Database(String url, String username, String password, ConnectionSource connectionSource) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.connectionSource = connectionSource;
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
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            stmt.setString(i + 1, params[i]);
        }

        QueryResult queryResult = null;
        try {
            stmt.execute();
            // Build the query result before we close the statement otherwise getResultSet will throw an exception
            queryResult = QueryResult.from(stmt.getResultSet());
        } finally {
            stmt.close();
        }
        return queryResult;
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
}
