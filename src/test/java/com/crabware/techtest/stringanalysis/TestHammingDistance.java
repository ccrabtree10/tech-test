package com.crabware.techtest.stringanalysis;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestHammingDistance {
    private static final String S1 = "D23W8MCCIZQOP9";
    private static final String S2 = "D236862CEZQOPS";
    private static final String LONG_STRING = "D236862CEZQOPSAAA";

    @Test
    public void correctHammingDistance() {
        assertEquals(5, HammingDistance.getHammingDistance(S1, S2));
        assertEquals(0, HammingDistance.getHammingDistance(S1, S1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStringForArg1NotAllowed() {
        HammingDistance.getHammingDistance(null, S1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStringForArg2NotAllowed() {
        HammingDistance.getHammingDistance(S1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void stringsOfDifferentLengthsNowAllowed() {
        HammingDistance.getHammingDistance(S1, LONG_STRING);
    }

}
