package com.crabware.techtest.databasecli.display;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Display {
    private static final int PADDING = 2;

    public static String render(ResultSet resultSet) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();
        StringBuilder buf = new StringBuilder();
        Map<Integer, Integer> columnWidths = new HashMap<>();

        for (int col = 1; col < columnCount + 1; col++) {
            columnWidths.put(col, resultSet.getMetaData().getColumnName(col).length());
        }

        while (resultSet.next()) {
            for (int col = 1; col < columnCount + 1; col++) {
                String data = resultSet.getString(col);
                if (data != null && data.length() > columnWidths.get(col)) {
                    columnWidths.put(col, data.length());
                }
            }
        }

        resultSet.beforeFirst();

        for (int col = 1; col < columnCount + 1; col++) {
            buf.append(rightPad(resultSet.getMetaData().getColumnName(col), columnWidths.get(col) + PADDING));
        }
        buf.append("\n");

        while (resultSet.next()) {
            for (int col = 1; col < columnCount + 1; col++) {
                buf.append(rightPad(resultSet.getString(col), columnWidths.get(col) + PADDING));
            }
            buf.append("\n");
        }
        return buf.toString();
    }

    public static String rightPad(String s, int length) {
        StringBuilder buf = new StringBuilder();
        buf.append(s);
        while(buf.length() < length) {
            buf.append(' ');
        }
        return buf.toString();
    }

}
