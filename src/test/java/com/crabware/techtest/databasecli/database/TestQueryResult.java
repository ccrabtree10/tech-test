package com.crabware.techtest.databasecli.database;

import com.crabware.techtest.databasecli.ResultSetMock;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestQueryResult {

    @Test
    public void from() throws SQLException {
        String[] columnNames = new String[]{"food", "tastiness", "colour"};
        String[][] data = new String[][]{
                new String[]{"Carrots", "5", "orange"},
                new String[]{"Chips", "59", "yellow"},
                new String[]{"Doner kebab", "999", "brown"},
                new String[]{"Falafel", "167", "brown"},
        };

        ResultSet resultSet = ResultSetMock.create(columnNames, data);
        QueryResult queryResult = QueryResult.from(resultSet);
        List<Map<String, String>> rows = queryResult.getRows();
        assertEquals(new HashSet(Arrays.asList("food", "tastiness", "colour")), queryResult.getHeaders());
        assertEquals(11, queryResult.getColumnWidth("food"));
        assertEquals(9, queryResult.getColumnWidth("tastiness"));
        assertEquals(6, queryResult.getColumnWidth("colour"));
        assertEquals("Carrots", rows.get(0).get("food"));
        assertEquals("59", rows.get(1).get("tastiness"));
        assertEquals("brown", rows.get(3).get("colour"));
    }
}
