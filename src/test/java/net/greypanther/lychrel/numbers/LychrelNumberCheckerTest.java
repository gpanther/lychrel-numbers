package net.greypanther.lychrel.numbers;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public final class LychrelNumberCheckerTest {
    private final long seed;
    private final long iterationCount;
    private final long digitCount;

    public LychrelNumberCheckerTest(long seed, long iterationCount, long digitCount) {
        this.seed = seed;
        this.iterationCount = iterationCount;
        this.digitCount = digitCount;
    }

    @Test
    public void testBigIntegerChecker() {
        Result result = BigIntegerChecker.check(seed, iterationCount + 1);
        assertEquals(new Result(true, iterationCount, digitCount), result);
    }

    @Test
    public void testByteArrayChecker() {
        Result result = new ByteArrayChecker().check(seed, iterationCount + 1);
        assertEquals(new Result(true, iterationCount, digitCount), result);
    }

    @Test
    public void testCompactDigitStoreChecker() {
        Result result = new CompactDigitStoreChecker().check(seed, iterationCount + 1);
        assertEquals(new Result(true, iterationCount, digitCount), result);
    }

    @Test
    public void testCompactDigitStoreCheckerWithInlining() {
        Result result = new CompactDigitStoreCheckerWithInlining().check(seed, iterationCount + 1);
        assertEquals(new Result(true, iterationCount, digitCount), result);
    }

    @Test
    public void testCompactDigitStoreCheckerWithAlternativeCarryCheck() {
        Result result = new CompactDigitStoreCheckerWithAlternativeCarryCheck().check(seed, iterationCount + 1);
        assertEquals(new Result(true, iterationCount, digitCount), result);
    }

    @Parameters(name = "{0} should be a palyndrom after {1} iteration(s)")
    public static Collection<Object[]> data() {
        // useful link: http://www.jasondoucette.com/pal/<seed>
        return Arrays.asList(new Object[][] { { 1, 0, 1 }, { 56, 1, 3 }, { 57, 2, 3 }, { 59, 3, 4 }, { 89, 24, 13 },
                { 10911, 55, 28 }, { 9218, 20, 13 }, { 1186060307891929990L, 261, 119 } });
    }
}
