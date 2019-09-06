package com.crabware.techtest.databasecli;

import com.crabware.techtest.databasecli.database.QueryResult;
import com.crabware.techtest.databasecli.display.Display;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class DatabaseCli {
    private static final String DEFAULT_PROPERTIES_FILENAME = "database.properties";
    private static final String PROPERTY_URL = "database.url";
    private static final String PROPERTY_USERNAME = "database.username";
    private static final String PROPERTY_PASSWORD = "database.password";
    private static final String PROPERTY_DRIVER_CLASS = "database.driverClass";

    public static void main(String[] args) throws ClassNotFoundException {
        String url = null;
        String username = null;
        String password = null;
        String driverClass = null;

        InputStream propertiesStream = ClassLoader.getSystemResourceAsStream(DEFAULT_PROPERTIES_FILENAME);
        if (propertiesStream != null) {
            Properties props = new Properties();
            try {
                props.load(propertiesStream);
                url = props.getProperty(PROPERTY_URL);
                username = props.getProperty(PROPERTY_USERNAME);
                password = props.getProperty(PROPERTY_PASSWORD);
                driverClass = props.getProperty(PROPERTY_DRIVER_CLASS);
                if (Arrays.asList(url, username, password, driverClass).contains(null)) {
                    StringBuilder buf = new StringBuilder();
                    buf.append("One of the required properties is null:\n");

                    for (String prop : new String[]{PROPERTY_URL, PROPERTY_USERNAME, PROPERTY_DRIVER_CLASS}) {
                        buf.append(prop + "=" + props.get(prop) + "\n");
                    }

                    buf.append(PROPERTY_PASSWORD + "=" + (password == null ? password : "<removed>") + "\n");
                    System.out.println(buf);
                    System.exit(1);
                }
            } catch (IOException e) {
                System.out.println("Couldn't read from properties file " + DEFAULT_PROPERTIES_FILENAME + ": " + e.getMessage());
                System.exit(1);
            }
        }


        Class.forName(driverClass);

        Connection con = null;
        try {
            con = DriverManager.getConnection(url, username, password);
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery("select * from foodmart.employee");

            System.out.println(Display.render(QueryResult.from(results)));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static List<String> getColumnNames(ResultSetMetaData metaData) throws SQLException {
        List<String> columnNames = new ArrayList<>();
        for (int col = 0; col < metaData.getColumnCount(); col++) {
            columnNames.add(metaData.getColumnName(col + 1));
        }

        return columnNames;
    }


}
