package com.crabware.techtest.part1;

public class StringAnalysis {

    private StringAnalysis() {}

    public static int findDifferences(String s1, String s2) {
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("Strings must not be null. s1=" + s1 + ", s2=" + s2);
        }
        int upperBound = s1.length() < s2.length() ? s1.length() : s2.length();
        int differenceCount = 0;

        for (int i = 0; i < upperBound; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                differenceCount++;
            }
        }

        return differenceCount;
    }
}
