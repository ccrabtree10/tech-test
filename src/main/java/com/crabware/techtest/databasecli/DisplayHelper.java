package com.crabware.techtest.databasecli;

import com.crabware.techtest.databasecli.util.QueryResult;
import java.util.*;

/**
 * A helper class for rendering a <code>QueryResult</code> to a table.
 */
public class DisplayHelper {
    private static final int PADDING = 2;

    private DisplayHelper() {}

    /**
     * Render the query result to a table with the given column order.
     *
     * @param queryResult    the query result
     * @param orderedColumnNames a list of column names specifying their order in the resultant table
     * @return the rendered table
     */
    public static String render(QueryResult queryResult, String[] orderedColumnNames) {
        StringBuilder output = new StringBuilder();

        for (String header : orderedColumnNames) {
            output.append(rightPad(header, queryResult.getColumnWidth(header) + PADDING));
        }
        output.append(System.lineSeparator());

        for (Map<String, String> row : queryResult.getRows()) {
            for (String header : orderedColumnNames) {
                output.append(rightPad(row.get(header), queryResult.getColumnWidth(header) + PADDING));
            }
            output.append(System.lineSeparator());
        }

        return output.toString();
    }

    /**
     * Render the query result to a table.
     *
     * @param queryResult the query result
     * @return the rendered table
     */
    public static String render(QueryResult queryResult) {
        return render(queryResult, queryResult.getColumnNames().toArray(new String[0]));
    }


    /**
     * Right pad a string with whitespace.
     *
     * @param s      the string
     * @param length the target length of the string
     * @return the right-padded string
     */
    public static String rightPad(String s, int length) {
        StringBuilder output = new StringBuilder();
        output.append(s);
        while(output.length() < length) {
            output.append(' ');
        }
        return output.toString();
    }

}
