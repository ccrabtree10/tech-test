package com.crabware.techtest.databasecli.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class QueryResult {
    private final Set<String> headers;
    private final List<Map<String, String>> rows;
    private final Map<String, Integer> columnWidths;

    private QueryResult(Set<String> headers, List<Map<String, String>> rows, Map<String, Integer> columnWidths) {
        this.headers = headers;
        this.rows = rows;
        this.columnWidths = columnWidths;
    }

    public Set<String> getHeaders() {
        return headers;
    }

    public List<Map<String, String>> getRows() {
        return rows;
    }

    public Map<String, Integer> getColumnWidths() {
        return columnWidths;
    }

    public int getColumnWidth(String header) {
        return columnWidths.get(header);
    }

    public static QueryResult from(ResultSet resultSet) throws SQLException {
        Set<String> headers = new HashSet<>();
        Map<String, Integer> columnWidths = new HashMap<>();
        List<Map<String, String>> rows = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();

        for (int col = 1; col < metaData.getColumnCount() + 1; col++) {
            String header = metaData.getColumnName(col);
            headers.add(header);
            columnWidths.put(header, header.length());
        }

        while (resultSet.next()) {
            Map<String, String> row = new HashMap<>();
            for (String header : headers) {
                String data = resultSet.getString(header);
                row.put(header, data);
                if (data != null && data.length() > columnWidths.get(header)) {
                    columnWidths.put(header, data.length());
                }
            }
            rows.add(row);
        }

        return new QueryResult(headers, rows, columnWidths);
    }
}
