package com.crabware.techtest.databasecli;

import com.crabware.techtest.databasecli.util.Database;
import com.crabware.techtest.databasecli.util.DriverManagerConnectionSource;
import com.crabware.techtest.databasecli.util.QueryResult;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A command line interface to a database.
 */
public class Cli {
    private static final String USAGE = "Usage: <java> -jar <dbcli.jar> DEPARTMENT PAY_TYPE EDUCATION_LEVEL";
    private String department;
    private String payType;
    private String educationLevel;
    private String url = null;
    private String username = null;
    private String password = null;

    /**
     * The entry point of the application.
     *
     * @param args Should be 3 arguments: department id, pay type, education level
     */
    public static void main(String[] args) {
        Cli databaseCli = new Cli();
        try {
           databaseCli.run(args);
        } catch (IOException ioe) {
            printAndExit("Error loading properties: " + ioe.getMessage());
        } catch (SQLException sqle) {
            printAndExit("Database error: " + sqle.getMessage());
        }
    }

    /**
     * Execute a query against the database to obtain a list of employees given the department id, pay type and
     * education level.
     *
     * @param args the command line arguments
     * @throws IOException  if there is a problem loading the properties
     * @throws SQLException if there is an error while communicating with the database
     */
    public void run(String[] args) throws IOException, SQLException {
        loadProperties("databasecli.properties");
        parseArgs(args);

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
     * Load the properties and extract the database url, username and password.
     *
     * @throws IOException if the properties file cannot be found or there is an error whilst reading
     */
    protected void loadProperties(String propertiesFilename) throws IOException {
        InputStream propertiesStream = ClassLoader.getSystemResourceAsStream(propertiesFilename);
        if (propertiesStream == null) {
            throw new IOException("Could not find properties file " + propertiesFilename + " on class path");
        } else {
            Properties properties = new Properties();
            properties.load(propertiesStream);
            url = properties.getProperty("database.url");
            username = properties.getProperty("database.username");
            password = properties.getProperty("database.password");
        }
    }

    /**
     * Parse the command line arguments.
     *
     * @throws IllegalArgumentException if not all of the required arguments are supplied
     */
    protected void parseArgs(String[] args) {
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
