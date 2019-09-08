package com.crabware.techtest.databasecli;

import com.crabware.techtest.databasecli.databaseutil.Database;
import com.crabware.techtest.databasecli.databaseutil.DriverManagerConnectionSource;
import com.crabware.techtest.databasecli.databaseutil.QueryResult;
import com.crabware.techtest.databasecli.display.DisplayHelper;
import com.crabware.techtest.databasecli.exceptions.PropertiesException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;

/**
 * The type Database cli.
 */
public class DatabaseCli {
    private static final String DEFAULT_PROPERTIES_FILENAME = "database.properties";
    private static final String PROPERTY_URL = "database.url";
    private static final String PROPERTY_USERNAME = "database.username";
    private static final String PROPERTY_PASSWORD = "database.password";
    private static final String USAGE = "Usage: <java> " + DatabaseCli.class.getName() + " DEPARTMENT PAY_TYPE EDUCATION_LEVEL";
    private final String[] args;
    private String department;
    private String payType;
    private String educationLevel;
    private String url = null;
    private String username = null;
    private String password = null;

    /**
     * Instantiates a new Database cli.
     *
     * @param args Should be 3 arguments: department, pay type, education level
     */
    public DatabaseCli(String[] args) {
        this.args = args;
    }

    /**
     * The entry point of the application.
     *
     * @param args Should be 3 arguments: department, pay type, education level
     */
    public static void main(String[] args) {
        DatabaseCli databaseCli = new DatabaseCli(args);
        try {
           databaseCli.run();
        } catch (PropertiesException pe) {
            printAndExit("Error loading properties: " + pe.getMessage());
        } catch (SQLException sqle) {
            printAndExit("Database error: " + sqle.getMessage());
        }
    }

    /**
     * Execute the program.
     *
     * @throws PropertiesException if there is a problem loading the properties
     * @throws SQLException        if there is an error while communicating with the database
     */
    public void run() throws PropertiesException, SQLException {
        loadProperties();
        parseArgs();

        Database database = Database.from(url, username, password, new DriverManagerConnectionSource());
        QueryResult queryResult = null;

        try {
            database.connect();
            queryResult = database.getEmployees(department, payType, educationLevel);
        } finally {
            database.disconnect();
        }

        if (queryResult != null) {
            System.out.println("Employees:");
            System.out.println(DisplayHelper.render(queryResult));
        }
    }

    private void loadProperties() throws PropertiesException {
        InputStream propertiesStream = ClassLoader.getSystemResourceAsStream(DEFAULT_PROPERTIES_FILENAME);
        if (propertiesStream == null) {
            throw new PropertiesException("Could not find " + DEFAULT_PROPERTIES_FILENAME + " on class path");
        } else {
            Properties properties = new Properties();
            try {
                properties.load(propertiesStream);
                url = properties.getProperty(PROPERTY_URL);
                username = properties.getProperty(PROPERTY_USERNAME);
                password = properties.getProperty(PROPERTY_PASSWORD);
                if (Arrays.asList(url, username, password).contains(null)) {
                    StringBuilder message = new StringBuilder();
                    message.append("One of the required properties is null:\n");

                    for (String prop : new String[]{PROPERTY_URL, PROPERTY_USERNAME}) {
                        message.append(prop + "=" + properties.get(prop) + "\n");
                    }

                    message.append(PROPERTY_PASSWORD + "=" + (password == null ? password : "<removed>") + "\n");
                    throw new PropertiesException(message.toString());
                }
            } catch (IOException e) {
                throw new PropertiesException("Couldn't read from properties file " + DEFAULT_PROPERTIES_FILENAME + ": " + e.getMessage(), e);
            }
        }
    }

    private void parseArgs() {
        if (args.length < 3) {
            throw new IllegalArgumentException("Expecting 3 arguments, only " + args.length + " supplied");
        }

        department = args[0];
        payType = args[1];
        educationLevel = args[2];
    }

    private static void printAndExit(String string) {
        System.out.println(string);
        System.exit(1);
    }
}
