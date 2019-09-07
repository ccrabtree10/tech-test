package com.crabware.techtest.databasecli.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public void connect() throws SQLException {
        if (connection == null) {
            connection = connectionSource.getConnection(url, username, password);
        }
    }

    public void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public QueryResult executeStatement(String query, String[] params) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query, params);
        QueryResult queryResult = null;
        try {
            stmt.execute();
            queryResult = QueryResult.from(stmt.getResultSet());
        } finally {
            stmt.close();
        }
        return queryResult;
    }

    public static Database from(String url, String username, String password, ConnectionSource connectionSource) {
        return new Database(url, username, password, connectionSource);
    }
}
