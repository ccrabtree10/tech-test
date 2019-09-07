package com.crabware.techtest.stringanalysis;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestStringAnalysis {
    private static final String S1 = "D23W8MCCIZQOP9";
    private static final String S2 = "D236862CEZQOPS";
    private static final String LONG_STRING = "D236862CEZQOPSAAA";

    @Test
    public void correctHammingDistance() {
        assertEquals(5, StringAnalysis.getHammingDistance(S1, S2));
        assertEquals(0, StringAnalysis.getHammingDistance(S1, S1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStringForArg1NotAllowed() {
        StringAnalysis.getHammingDistance(null, S1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStringForArg2NotAllowed() {
        StringAnalysis.getHammingDistance(S1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void stringsOfDifferentLengthsNowAllowed() {
        StringAnalysis.getHammingDistance(S1, LONG_STRING);
    }

}
