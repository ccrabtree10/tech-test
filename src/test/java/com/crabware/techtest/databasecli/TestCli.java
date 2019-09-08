package com.crabware.techtest.databasecli;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestCli {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test(expected = IllegalArgumentException.class)
    public void exceptionWhenNotEnoughArgsPassed() {
        Cli cli = new Cli();
        cli.parseArgs(new String[]{"a", "b"});
    }

    @Test(expected = PropertiesException.class)
    public void exceptionWhenNoPropertiesFileExists() throws PropertiesException {
        Cli cli = new Cli();
        cli.loadProperties("thisFileDoesNot.Exist");
    }

    @Test
    public void exceptionWhenRequiredPropertyIsMissing() throws PropertiesException {
        thrown.expect(PropertiesException.class);
        thrown.expectMessage("password=null");
        Cli cli = new Cli();
        cli.loadProperties("hasMissingPassword.properties");
    }


}
