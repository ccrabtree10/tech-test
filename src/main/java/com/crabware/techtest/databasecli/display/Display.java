package com.crabware.techtest.databasecli.display;

import com.crabware.techtest.databasecli.database.QueryResult;

import java.sql.SQLException;
import java.util.*;

public class Display {
    private static final int PADDING = 2;

    public static String render(QueryResult queryResult, String[] orderedHeaders) throws SQLException {
        StringBuilder buf = new StringBuilder();

        for (String header : orderedHeaders) {
            buf.append(rightPad(header, queryResult.getColumnWidth(header) + PADDING));
        }
        buf.append(System.lineSeparator());

        for (Map<String, String> row : queryResult.getRows()) {
            for (String header : orderedHeaders) {
                buf.append(rightPad(row.get(header), queryResult.getColumnWidth(header) + PADDING));
            }
            buf.append(System.lineSeparator());
        }

        return buf.toString();
    }

    public static String render(QueryResult queryResult) throws SQLException {
        return render(queryResult, queryResult.getHeaders().toArray(new String[0]));
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
