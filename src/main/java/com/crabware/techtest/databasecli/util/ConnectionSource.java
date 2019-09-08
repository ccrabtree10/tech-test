package com.crabware.techtest.databasecli.util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A source of JDBC connections.
 */
public interface ConnectionSource {
    /**
     * Gets a connection to the specified database.
     *
     * @param url      the url of the database
     * @param username the username
     * @param password the password
     * @return the connection
     * @throws SQLException if an error occurs whilst obtaining the database connection
     */
    Connection getConnection(String url, String username, String password) throws SQLException;
}
