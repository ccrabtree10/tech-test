package com.crabware.techtest.databasecli.exceptions;

public class PropertiesException extends Exception {
    public PropertiesException(String message) {
        super(message);
    }

    public PropertiesException(String message, Throwable cause) {
        super(message, cause);
    }
}
