package com.crabware.techtest.databasecli;

import com.crabware.techtest.databasecli.database.Database;
import com.crabware.techtest.databasecli.database.DriverManagerConnectionSource;
import com.crabware.techtest.databasecli.database.FoodMartHelper;
import com.crabware.techtest.databasecli.database.QueryResult;
import com.crabware.techtest.databasecli.display.Display;
import com.crabware.techtest.databasecli.exceptions.ArgumentException;
import com.crabware.techtest.databasecli.exceptions.PropertiesException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;

public class DatabaseCli {
    private static final String DEFAULT_PROPERTIES_FILENAME = "database.properties";
    private static final String PROPERTY_URL = "database.url";
    private static final String PROPERTY_USERNAME = "database.username";
    private static final String PROPERTY_PASSWORD = "database.password";
    private static final String PROPERTY_DRIVER_CLASS = "database.driverClass";
    private static final String USAGE = "Usage: <java> DEPARTMENT PAY_TYPE EDUCATION_LEVEL";
    private final String[] args;
    private String department;
    private String payType;
    private String educationLevel;
    private Properties properties;
    private String url = null;
    private String username = null;
    private String password = null;

    public DatabaseCli(String[] args) {
        this.args = args;
    }

    public static void main(String[] args) {
        DatabaseCli databaseCli = new DatabaseCli(args);
        databaseCli.run();
    }

    public static void printAndExit(String string) {
        System.out.println(string);
        System.exit(1);
    }

    public void run() {
        try {
            loadProperties();
            parseArgs();
        } catch (PropertiesException pe) {
            printAndExit("Error loading properties: " + pe.getMessage());
        } catch (ArgumentException ae) {
            printAndExit("Error parsing arguments: " + ae.getMessage());
        }

        Database database = Database.from(url, username, password, new DriverManagerConnectionSource());
        FoodMartHelper foodMartHelper = FoodMartHelper.from(database);
        QueryResult queryResult = null;
        try {
            database.connect();
            queryResult = foodMartHelper.getEmployees(department, payType, educationLevel);
        } catch (SQLException sqle) {
            printAndExit("Database error: " + sqle.getMessage());
        } finally {
            try {
                database.disconnect();
            } catch (SQLException e) {
                System.out.println("Database error while disconnecting: " + e.getMessage());
            }
        }

        if (queryResult != null) {
            System.out.println("Employees:");
            System.out.println(Display.render(queryResult));
        }
    }

    protected void loadProperties() throws PropertiesException {
        InputStream propertiesStream = ClassLoader.getSystemResourceAsStream(DEFAULT_PROPERTIES_FILENAME);
        if (propertiesStream == null) {
            throw new PropertiesException("Could not find " + DEFAULT_PROPERTIES_FILENAME + " on class path");
        } else {
            properties = new Properties();
            try {
                properties.load(propertiesStream);
                url = properties.getProperty(PROPERTY_URL);
                username = properties.getProperty(PROPERTY_USERNAME);
                password = properties.getProperty(PROPERTY_PASSWORD);
                if (Arrays.asList(url, username, password).contains(null)) {
                    StringBuilder message = new StringBuilder();
                    message.append("One of the required properties is null:\n");

                    for (String prop : new String[]{PROPERTY_URL, PROPERTY_USERNAME, PROPERTY_DRIVER_CLASS}) {
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

    protected void parseArgs() throws ArgumentException {
        if (args.length < 3) {
            throw new ArgumentException("Expecting 3 arguments, only " + args.length + " supplied");
        }

        department = args[0];
        payType = args[1];
        educationLevel = args[2];
    }




}
