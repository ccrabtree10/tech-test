package com.crabware.techtest.databasecli.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionSource {
    Connection getConnection(String url, String username, String password) throws SQLException;
}
