package com.crabware.techtest.databasecli.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * The result of a database query.
 */
public class QueryResult {
    private final Set<String> columnNames;
    private final List<Map<String, String>> rows;
    private final Map<String, Integer> columnWidths;

    private QueryResult(Set<String> columnNames, List<Map<String, String>> rows, Map<String, Integer> columnWidths) {
        this.columnNames = columnNames;
        this.rows = rows;
        this.columnWidths = columnWidths;
    }

    /**
     * Gets the column names.
     *
     * @return the column names
     */
    public Set<String> getColumnNames() {
        return columnNames;
    }

    /**
     * Gets the rows returned by the query.
     *
     * @return the rows
     */
    public List<Map<String, String>> getRows() {
        return rows;
    }

    /**
     * Gets the maximum number of characters used for the given column. Helpful when trying to display a query in a
     * table.
     *
     * @param columnName the column name
     * @return the column width
     */
    public int getColumnWidth(String columnName) {
        return columnWidths.get(columnName);
    }

    /**
     * Construct a <code>ResultQuery</code> from a <code>ResultSet</code>.
     *
     * @param resultSet the result set
     * @return the query result
     * @throws SQLException if an error occurs whilst accessing the result set
     */
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
