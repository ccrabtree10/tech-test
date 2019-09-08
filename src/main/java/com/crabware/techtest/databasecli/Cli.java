package com.crabware.techtest.databasecli;

import com.crabware.techtest.databasecli.util.Database;
import com.crabware.techtest.databasecli.util.DriverManagerConnectionSource;
import com.crabware.techtest.databasecli.util.QueryResult;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

/**
 * A command line interface to a database.
 */
public class Cli {
    private static final String DEFAULT_PROPERTIES_FILENAME = "database.properties";
    private static final String PROPERTY_URL = "database.url";
    private static final String PROPERTY_USERNAME = "database.username";
    private static final String PROPERTY_PASSWORD = "database.password";
    private static final String USAGE = "Usage: <java> -jar <dbcli.jar> DEPARTMENT PAY_TYPE EDUCATION_LEVEL";
    private final String[] args;
    private String department;
    private String payType;
    private String educationLevel;
    private String url = null;
    private String username = null;
    private String password = null;

    /**
     * Instantiates a new database command line interface.
     *
     * @param args Should be 3 arguments: department id, pay type, education level
     */
    public Cli(String[] args) {
        this.args = args;
    }

    /**
     * The entry point of the application.
     *
     * @param args Should be 3 arguments: department id, pay type, education level
     */
    public static void main(String[] args) {
        Cli databaseCli = new Cli(args);
        try {
           databaseCli.run();
        } catch (PropertiesException pe) {
            printAndExit("Error loading properties: " + pe.getMessage());
        } catch (SQLException sqle) {
            printAndExit("Database error: " + sqle.getMessage());
        }
    }

    /**
     * Execute a query against the database to obtain a list of employees given the department id, pay type and
     * education level.
     *
     * @throws PropertiesException if there is a problem loading the properties
     * @throws SQLException        if there is an error while communicating with the database
     */
    public void run() throws PropertiesException, SQLException {
        loadProperties();
        parseArgs();

        Database database = Database.from(url, username, password, new DriverManagerConnectionSource());
        QueryResult queryResult;

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

    /**
     * Attempts to get the properties as an input stream.
     *
     * @return the properties input stream. <code>null</code> is returned if no properties file can be found
     */
    protected InputStream getPropertiesInputStream() {
        return ClassLoader.getSystemResourceAsStream(DEFAULT_PROPERTIES_FILENAME);
    }

    /**
     * Load the properties and extract the database url, username and password.
     *
     * @throws PropertiesException if a required property is missing, the properties file does not exist, or there is
     * an error whilst reading from the properties file
     */
    protected void loadProperties() throws PropertiesException {
        InputStream propertiesStream = getPropertiesInputStream();
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

    /**
     * Parse the command line arguments.
     *
     * @throws IllegalArgumentException if not all of the required arguments are supplied
     */
    protected void parseArgs() {
        if (args.length < 3) {
            throw new IllegalArgumentException("Expecting 3 arguments, only " + args.length + " supplied\n" + USAGE);
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
