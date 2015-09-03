package net.greypanther.lychrel.numbers;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

public class ImplementationBenchmarks {
    private static final int MAX_VALUE_TO_CHECK = 1000;
    private static final int MAX_ITERATION_COUNT = 5000;

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    public void measureBigIntegerChecker(Blackhole bh) {
        for (int i = 0; i < MAX_VALUE_TO_CHECK; ++i) {
            bh.consume(BigIntegerChecker.check(i, MAX_ITERATION_COUNT).getIterationCount());
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    public void measureByteArrayChecker(Blackhole bh) {
        ByteArrayChecker checker = new ByteArrayChecker();
        for (int i = 0; i < MAX_VALUE_TO_CHECK; ++i) {
            bh.consume(checker.check(i, MAX_ITERATION_COUNT).getIterationCount());
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    public void measureCompactDigitStoreChecker(Blackhole bh) {
        CheckerWithDigitStore checker = new CompactDigitStoreChecker();
        for (int i = 0; i < MAX_VALUE_TO_CHECK; ++i) {
            bh.consume(checker.check(i, MAX_ITERATION_COUNT).getIterationCount());
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    public void measureCompactDigitStoreCheckerWithInlining(Blackhole bh) {
        CheckerWithDigitStore checker = new CompactDigitStoreCheckerWithInlining();
        for (int i = 0; i < MAX_VALUE_TO_CHECK; ++i) {
            bh.consume(checker.check(i, MAX_ITERATION_COUNT).getIterationCount());
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    public void measureCompactDigitStoreCheckerWithAlternativeCarryCheck(Blackhole bh) {
        CheckerWithDigitStore checker = new CompactDigitStoreCheckerWithAlternativeCarryCheck();
        for (int i = 0; i < MAX_VALUE_TO_CHECK; ++i) {
            bh.consume(checker.check(i, MAX_ITERATION_COUNT).getIterationCount());
        }
    }
}
