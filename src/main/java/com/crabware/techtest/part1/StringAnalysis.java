package com.crabware.techtest.part1;

public class StringAnalysis {

    private StringAnalysis() {}

    public static int findDifferences(String s1, String s2) {
        if (s1 == null || s2 == null) {
            // If either argument is null, we can't meaningfully analyse them
            throw new IllegalArgumentException("Strings must not be null. s1=" + s1 + ", s2=" + s2);
        }

        // Get the shortest string and only compare characters up to that point
        int upperBound = s1.length() < s2.length() ? s1.length() : s2.length();
        int differenceCount = 0;

        // Iterate the characters of both strings and compare each pair
        for (int i = 0; i < upperBound; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                differenceCount++;
            }
        }

        return differenceCount;
    }
}
