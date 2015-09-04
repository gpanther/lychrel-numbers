package net.greypanther.lychrel.numbers;

public final class CompactDigitStoreCheckerWithInlining extends CheckerWithDigitStore {
    private static final long MAX_NUMBER_OF_DIGITS = 2000 * 1024 * 1024;

    private final DigitStore digits = new DigitStore();
    private final FastBitSet bitSet = new FastBitSet(MAX_NUMBER_OF_DIGITS);

    @Override
    int getDigitAt(long index) {
        return digits.get(index);
    }

    @Override
    void init(long seed) {
        digits.clear();
        while (seed > 0) {
            int digit = (int) (seed % 10);
            seed = seed / 10;
            digits.append(digit);
        }
    }

    @Override
    long getDigitsCount() {
        return digits.getDigitLength();
    }

    @Override
    void storeHadCarry(long index, boolean value) {
        bitSet.set(index, value);
    }

    @Override
    boolean hadCarry(long index) {
        return bitSet.get(index);
    }

    @Override
    int addToDigitAt(long index, int value) {
        return digits.addToDigitAt(index, value);
    }

    @Override
    void appendDigit(int digit) {
        assert digit >= 0 && digit <= 9;
        digits.append(digit);
    }

    private static final class DigitStore {
        private final static int LOW_MASK = Integer.parseInt("00001111", 2);
        private final static int HIGH_MASK = Integer.parseInt("11110000", 2);

        private final byte[] digits = new byte[(int) (MAX_NUMBER_OF_DIGITS / 2)];
        private long digitLength;

        int get(long index) {
            int byteIndex = (int) (index >>> 1);
            int b = digits[byteIndex] & 0xFF;
            if ((index & 1) == 0) {
                return b & LOW_MASK;
            } else {
                return b >>> 4;
            }
        }

        void clear() {
            digitLength = 0;
        }

        int addToDigitAt(long index, int toAdd) {
            int carry, value;
            final int byteIndex = (int) (index >>> 1);
            final int b = digits[byteIndex] & 0xFF;
            if ((index & 1) == 0) {
                // inlined get
                value = b & LOW_MASK;
                value += toAdd;
                carry = value / 10;
                value = value % 10;
                // inlined set
                digits[byteIndex] = (byte) ((b & HIGH_MASK) | value);
            } else {
                value = b >>> 4;
                value += toAdd;
                carry = value / 10;
                value = value % 10;
                // inlined set
                digits[byteIndex] = (byte) ((value << 4) | (b & LOW_MASK));
            }

            return carry;
        }

        void set(long index, int digit) {
            assert digit >= 0 && digit <= 9;

            int byteIndex = (int) (index >>> 1);
            int b = digits[byteIndex] & 0xFF;
            if ((index & 1) == 0) {
                digits[byteIndex] = (byte) ((b & HIGH_MASK) | digit);
            } else {
                digits[byteIndex] = (byte) ((digit << 4) | (b & LOW_MASK));
            }
        }

        void append(int digit) {
            assert digit >= 0 && digit <= 9;
            set(digitLength, digit);
            digitLength++;
        }

        long getDigitLength() {
            return digitLength;
        }

        @Override
        public String toString() {
            char[] result = new char[(int) digitLength];
            for (int i = 0; i < digitLength; ++i) {
                result[i] = (char) ('0' + get(digitLength - i - 1));
            }
            return new String(result);
        }
    }

    private static final class FastBitSet {
        private final long[] bits;

        private FastBitSet(long maximumEntry) {
            this.bits = new long[(int) (maximumEntry / Long.SIZE + 1)];
        }

        boolean get(long index) {
            int arrayIndex = (int) (index >>> 64);
            int bitIndex = (int) (index & 63);
            long mask = 1L << bitIndex;
            return (bits[arrayIndex] & mask) != 0;
        }

        void set(long index, boolean value) {
            int arrayIndex = (int) (index >>> 64);
            int bitIndex = (int) (index & 63);
            long mask = 1L << bitIndex;
            if (value) {
                bits[arrayIndex] |= mask;
            } else {
                bits[arrayIndex] &= ~mask;
            }
        }
    }
}
