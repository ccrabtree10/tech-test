package com.crabware.techtest.databasecli;

import org.mockito.stubbing.Answer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class ResultSetMock {
    private int cursor = -1;
    private String[] columnNames;
    private String[][] data;

    private ResultSetMock(String[] columnNames, String[][] data) {
        this.columnNames = columnNames;
        this.data = data;
    }

    private ResultSet buildMock() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenAnswer(invocation -> columnNames.length);
        when(metaData.getColumnName(anyInt())).thenAnswer(invocation -> columnNames[(int) invocation.getArguments()[0] - 1]);
        when(metaData.getColumnDisplaySize(anyInt())).thenAnswer(invocation -> {
            int col = (int) invocation.getArguments()[0] - 1;
            int maxWidth = columnNames[col].length();
            for (String[] row : data) {
                if (row[col].length() > maxWidth) {
                    maxWidth = row[col].length();
                }
            }
            return maxWidth;
        });

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenAnswer(invocation -> {
            cursor++;
            return cursor < data.length;
        });
        when(resultSet.getMetaData()).thenReturn(metaData);
        when(resultSet.getString(anyInt())).thenAnswer(invocation -> data[cursor][(int) invocation.getArguments()[0] - 1]);
        when(resultSet.getString(anyString())).thenAnswer(invocation -> {
            String header = (String) invocation.getArguments()[0];
            int column = Arrays.asList(columnNames).indexOf(header);
            return data[cursor][column];
        });

        doAnswer(invocation -> {
            cursor = -1;
            return null;
        }).when(resultSet).beforeFirst();

        return resultSet;
    }

    public static ResultSet create(String[] columnNames, String[][] data) throws SQLException {
        return new ResultSetMock(columnNames, data).buildMock();
    }
}
