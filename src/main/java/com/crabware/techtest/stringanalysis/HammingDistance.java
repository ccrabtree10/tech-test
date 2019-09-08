package com.crabware.techtest.stringanalysis;

/**
 * Class for finding the hamming distance between strings.
 */
public class HammingDistance {

    private HammingDistance() {}

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("2 strings must be supplied");
        }

        System.out.println("Hamming distance: " + getHammingDistance(args[0], args[1]));
    }

    /**
     * Gets hamming distance.
     *
     * @param s1 the s 1
     * @param s2 the s 2
     * @return the hamming distance
     */
    public static int getHammingDistance(String s1, String s2) {
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("Strings must not be null. s1=" + s1 + ", s2=" + s2);
        }

        if (s1.length() != s2.length()) {
            throw new IllegalArgumentException(
                    "String must be of equal length. s1 length=" + s1.length() + ", s2 length=" + s2.length());
        }

        int differenceCount = 0;

        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                differenceCount++;
            }
        }

        return differenceCount;
    }
}
