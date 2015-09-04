package net.greypanther.lychrel.numbers;

public abstract class CheckerWithDigitStore {
    public final Result check(long seed, long maxIterationCount) {
        init(seed);

        for (long iterationIndex = 0; iterationIndex < maxIterationCount; ++iterationIndex) {
            if (isPalindrome()) {
                return new Result(true, iterationIndex, getDigitsCount());
            }

            if ((iterationIndex & 1023) == 0 & iterationIndex != 0) {
                System.out.format("Iteration: %,d / Digit count: %,d\n", iterationIndex, getDigitsCount());
            }

            long length = getDigitsCount();
            long antipodeDigitIndex = length - 1;
            long halfLength = isEven(length) ? length / 2 : length / 2 + 1;
            int carry = 0;

            for (long j = 0; j < halfLength; ++j) {
                int d2 = getDigitAt(antipodeDigitIndex--);
                storeHadCarry(j, carry > 0);
                carry = addToDigitAt(j, d2 + carry);
                assert carry == 0 | carry == 1;
            }

            for (long j = halfLength; j < length; ++j) {
                int d1 = getDigitAt(j);
                int d2 = getDigitAt(antipodeDigitIndex);
                d2 -= d1;
                if (hadCarry(antipodeDigitIndex)) {
                    d2 -= 1;
                }
                if (d2 < 0) {
                    d2 += 10;
                }
                assert d2 >= 0 & d2 <= 9;
                carry = addToDigitAt(j, d2 + carry);
                antipodeDigitIndex--;
            }

            if (carry > 0) {
                appendDigit(carry);
            }
        }

        return new Result(false, maxIterationCount, getDigitsCount());
    }

    private boolean isPalindrome() {
        long length = getDigitsCount();
        long i = 0;
        long j = length - 1;
        while (i < j) {
            int d1 = getDigitAt(i), d2 = getDigitAt(j);
            if (d1 != d2) {
                return false;
            }
            i++;
            j--;
        }

        return true;
    }

    private static boolean isEven(long value) {
        return (value & 1) == 0;
    }

    abstract int getDigitAt(long index);

    abstract void init(long seed);

    abstract long getDigitsCount();

    abstract void storeHadCarry(long index, boolean value);

    abstract boolean hadCarry(long index);

    abstract int addToDigitAt(long index, int value);

    abstract void appendDigit(int digit);
}
