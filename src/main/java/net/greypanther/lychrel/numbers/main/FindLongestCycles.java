package net.greypanther.lychrel.numbers.main;

import net.greypanther.lychrel.numbers.CheckerWithDigitStore;
import net.greypanther.lychrel.numbers.CompactDigitStoreCheckerWithInlining;
import net.greypanther.lychrel.numbers.Result;

public final class FindLongestCycles {
    private static final long MAX_ITERATIONS = 500;

    public static void main(String[] args) {
        CheckerWithDigitStore checker = new CompactDigitStoreCheckerWithInlining();

        long max = Long.MIN_VALUE;
        for (long i = 0; i < Long.MAX_VALUE - 1; i++) {
            Result result = checker.check(i, MAX_ITERATIONS);
            if (result.wasCycleFound()) {
                if (result.getIterationCount() > max) {
                    System.out.println("Found new longest cycle:");
                    System.out.println(result);
                    max = result.getIterationCount();
                }
            } else {
                System.out.println("Possyble Lychrel number: " + i);
            }
        }
    }

}
