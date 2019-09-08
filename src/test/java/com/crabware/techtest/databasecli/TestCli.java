package com.crabware.techtest.databasecli;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestCli {

    @Test(expected = IllegalArgumentException.class)
    public void exceptionWhenNotEnoughArgsPassed() {
        Cli cli = new Cli(new String[]{"a", "b"});
        cli.parseArgs();
    }

    @Test(expected = PropertiesException.class)
    public void propertiesExceptionWhenPasswordNotSuppllied() throws PropertiesException {
        Cli cli = spy(new Cli(new String[]{"a", "b", "c"}));
        String properties = "database.url=jdbc:mysql://url.com\ndatabase.username=user";
        when(cli.getPropertiesInputStream()).thenReturn(new ByteArrayInputStream(properties.getBytes()));
        cli.loadProperties();
    }

    @Test(expected = PropertiesException.class)
    public void propertiesExceptionWhenNoPropertiesFileExists() throws PropertiesException {
        Cli cli = spy(new Cli(new String[]{"a", "b", "c"}));
        when(cli.getPropertiesInputStream()).thenReturn(null);
        cli.loadProperties();
    }
}
