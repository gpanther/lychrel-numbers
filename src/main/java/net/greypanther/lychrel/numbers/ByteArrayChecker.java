package net.greypanther.lychrel.numbers;

import java.util.BitSet;

public final class ByteArrayChecker extends CheckerWithDigitStore {
    private static final int MAX_NUMBER_OF_DIGITS = 1024 * 1024;

    private final byte[] bytes = new byte[MAX_NUMBER_OF_DIGITS];
    private final BitSet carry = new BitSet(MAX_NUMBER_OF_DIGITS);
    private int digitCount;

    @Override
    int getDigitAt(long index) {
        return bytes[(int) index];
    }

    @Override
    void init(long seed) {
        digitCount = 0;
        while (seed > 0) {
            int digit = (int) (seed % 10);
            seed = seed / 10;
            appendDigit(digit);
        }
    }

    @Override
    long getDigitsCount() {
        return digitCount;
    }

    @Override
    void storeHadCarry(long index, boolean value) {
        carry.set((int) index, value);
    }

    @Override
    boolean hadCarry(long index) {
        return carry.get((int) index);
    }

    @Override
    int addToDigitAt(long index, int value) {
        int b = bytes[(int) index];
        b += value;
        int carry = b / 10;
        b = b % 10;
        bytes[(int) index] = (byte) b;
        return carry;
    }

    @Override
    void appendDigit(int digit) {
        assert digit >= 0 && digit <= 9;
        bytes[digitCount++] = (byte) digit;
    }

}
