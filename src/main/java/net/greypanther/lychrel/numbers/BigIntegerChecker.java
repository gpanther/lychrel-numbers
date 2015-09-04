package net.greypanther.lychrel.numbers;

import java.math.BigInteger;

public final class BigIntegerChecker {
    private BigIntegerChecker() {
        throw new UnsupportedOperationException("Should never be instantiated");
    }

    public static Result check(long seed, long maxIterationCount) {
        BigInteger value = BigInteger.valueOf(seed);

        int longestLength = 0;
        for (long iterationIndex = 0; iterationIndex < maxIterationCount; ++iterationIndex) {
            String digits = value.toString();
            longestLength = digits.length();
            if (isPalindrome(digits)) {
                return new Result(true, iterationIndex, longestLength);
            }

            value = value.add(new BigInteger(new StringBuffer(digits).reverse().toString()));
        }

        return new Result(false, maxIterationCount, longestLength);
    }

    private static boolean isPalindrome(String digits) {
        int i = 0, j = digits.length() - 1;
        while (i < j) {
            if (digits.charAt(i++) != digits.charAt(j--)) {
                return false;
            }
        }
        return true;
    }
}
