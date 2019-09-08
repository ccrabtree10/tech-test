package com.crabware.techtest.databasecli;

/**
 * Thrown to indicate an error has happened whilst trying to read/parse a properties file.
 */
public class PropertiesException extends Exception {
    public PropertiesException(String message) {
        super(message);
    }

    public PropertiesException(String message, Throwable cause) {
        super(message, cause);
    }
}