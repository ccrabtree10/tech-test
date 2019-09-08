package com.crabware.techtest.databasecli;

import org.junit.Test;

import java.io.IOException;

public class TestCli {

    @Test(expected = IllegalArgumentException.class)
    public void exceptionWhenNotEnoughArgsPassed() {
        Cli cli = new Cli();
        cli.parseArgs(new String[]{"a", "b"});
    }

    @Test(expected = IOException.class)
    public void exceptionWhenNoPropertiesFileExists() throws IOException {
        Cli cli = new Cli();
        cli.loadProperties("thisFileDoesNot.Exist");
    }
}
