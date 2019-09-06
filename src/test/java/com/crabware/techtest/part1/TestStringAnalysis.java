package com.crabware.techtest.part1;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestStringAnalysis {
    public static final String S1 = "D23W8MCCIZQOP9";
    public static final String S2 = "D236862CEZQOPS";
    public static final String LONG_STRING = "D236862CEZQOPSAAA";

    @Test
    public void example() {
        assertEquals(5, StringAnalysis.findDifferences(S1, S2));
        assertEquals(0, StringAnalysis.findDifferences(S1, S1));
    }

    @Test
    public void longString() {
        assertEquals(5, StringAnalysis.findDifferences(S1, LONG_STRING));
        assertEquals(0, StringAnalysis.findDifferences(LONG_STRING, S2));
    }

    @Test
    public void t() {

    }


}
