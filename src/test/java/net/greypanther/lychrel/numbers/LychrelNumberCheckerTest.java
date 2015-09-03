package net.greypanther.lychrel.numbers;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.greypanther.lychrel.numbers.LychrelNumberChecker;

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
    public void testLychrelNumberChecker() {
        LychrelNumberChecker.Result result = LychrelNumberChecker.check(seed, iterationCount + 1);
        assertEquals(new LychrelNumberChecker.Result(true, iterationCount, digitCount), result);
    }

    @Parameters(name = "{0} should be a palyndrom after {1} iteration(s)")
    public static Collection<Object[]> data() {
        // useful link: http://www.jasondoucette.com/pal/<seed>
        return Arrays.asList(new Object[][] { { 1, 0, 1 }, { 56, 1, 3 }, { 57, 2, 3 }, { 59, 3, 4 }, { 89, 24, 13 },
                { 10911, 55, 28 }, { 9218, 20, 13 }, { 1186060307891929990L, 261, 119 } });
    }
}
