package com.crabware.techtest.databasecli.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A connection source where connections are obtained from <code>DriverManager</code>.
 */
public class DriverManagerConnectionSource implements ConnectionSource {
    @Override
    public Connection getConnection(String url, String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
