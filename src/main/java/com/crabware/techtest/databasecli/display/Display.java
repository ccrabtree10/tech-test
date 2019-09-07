package com.crabware.techtest.databasecli.display;

import com.crabware.techtest.databasecli.database.QueryResult;

import java.sql.SQLException;
import java.util.*;

public class Display {
    private static final int PADDING = 2;

    public static String render(QueryResult queryResult, String[] orderedHeaders) throws SQLException {
        StringBuilder output = new StringBuilder();

        for (String header : orderedHeaders) {
            output.append(rightPad(header, queryResult.getColumnWidth(header) + PADDING));
        }
        output.append(System.lineSeparator());

        for (Map<String, String> row : queryResult.getRows()) {
            for (String header : orderedHeaders) {
                output.append(rightPad(row.get(header), queryResult.getColumnWidth(header) + PADDING));
            }
            output.append(System.lineSeparator());
        }

        return output.toString();
    }

    public static String render(QueryResult queryResult) throws SQLException {
        return render(queryResult, queryResult.getHeaders().toArray(new String[0]));
    }


    public static String rightPad(String s, int length) {
        StringBuilder output = new StringBuilder();
        output.append(s);
        while(output.length() < length) {
            output.append(' ');
        }
        return output.toString();
    }

}
