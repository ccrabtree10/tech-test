package com.crabware.techtest.part1;

public class StringAnalysis {

    public static int findDifferences(String s1, String s2) {
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
