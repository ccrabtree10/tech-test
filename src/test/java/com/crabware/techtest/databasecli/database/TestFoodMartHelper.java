package com.crabware.techtest.databasecli.database;

import com.crabware.techtest.databasecli.ResultSetMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestFoodMartHelper {


    @Test
    public void getInfo() throws SQLException {
        String[] columnNames = new String[]{"name", "age", "weight"};
        String[][] data = new String[][]{
                new String[]{"Jack Top", "34", "78"},
                new String[]{"Milly Sanders", "19", "103"},
                new String[]{"Hugh Jarse", "27", "192"}
        };
        ResultSet resultSet = ResultSetMock.create(columnNames, data);

        Connection connection = mock(Connection.class);
        ConnectionSource connectionSource = mock(ConnectionSource.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        when(connectionSource.getConnection("url", "user", "pass")).thenReturn(connection);
        when(connection.prepareStatement(anyString(), any(String[].class))).thenReturn(stmt);
        when(stmt.getResultSet()).thenReturn(resultSet);

        Database db = Database.from("url", "user", "pass", connectionSource);
        db.connect();

        FoodMartHelper helper = FoodMartHelper.from(db);
        String department = "";
        String payType = "";
        String educationLevel = "";
        QueryResult queryResult = helper.getEmployees(department, payType, educationLevel);
        assertEquals(new HashSet<>(Arrays.asList(new String[]{"name", "age", "weight"})), queryResult.getHeaders());
    }
}
