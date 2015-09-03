package net.greypanther.lychrel.numbers;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

final class LychrelNumberChecker {
    private static final long MAX_NUMBER_OF_DIGITS = 1000 * 1000 * 1000;

    private LychrelNumberChecker() {
        throw new UnsupportedOperationException("Should never be instantiated");
    }

    static Result check(long seed, long maxIterationCount) {
        DigitStore digits = new DigitStore();
        FastBitSet hadCarry = new FastBitSet(MAX_NUMBER_OF_DIGITS);

        copy(seed, digits);

        for (long iterationIndex = 0; iterationIndex < maxIterationCount; ++iterationIndex) {
            if (isPalindrome(digits)) {
                return new Result(true, iterationIndex, digits.getDigitLength());
            }

            if ((iterationIndex & 1023) == 0) {
                System.out.println("Iteration " + iterationIndex + " digit length " + digits.getDigitLength());
            }

            long length = digits.getDigitLength();
            long antipodeDigitIndex = length - 1;
            long halfLength = isEven(length) ? length / 2 : length / 2 + 1;
            int carry = 0;

            for (long j = 0; j < halfLength; ++j) {
                int d2 = digits.get(antipodeDigitIndex--);
                hadCarry.set(j, carry > 0);
                carry = digits.addToDigitAt(j, d2 + carry);
                assert carry == 0 | carry == 1;
            }

            for (long j = halfLength; j < length; ++j) {
                int d1 = digits.get(j);
                int d2 = digits.get(antipodeDigitIndex);
                d2 -= d1;
                if (hadCarry.get(antipodeDigitIndex)) {
                    d2 -= 1;
                }
                if (d2 < 0) {
                    d2 += 10;
                }
                assert d2 >= 0 & d2 <= 9;
                carry = digits.addToDigitAt(j, d2 + carry);
                antipodeDigitIndex--;
            }

            if (carry > 0) {
                digits.append(carry);
            }
        }

        return new Result(false, maxIterationCount, digits.getDigitLength());
    }

    private static boolean isEven(long value) {
        return (value & 1) == 0;
    }

    private static void copy(long seed, DigitStore digits) {
        while (seed > 0) {
            int digit = (int) (seed % 10);
            seed = seed / 10;
            digits.append(digit);
        }
    }

    private static boolean isPalindrome(DigitStore digits) {
        long length = digits.getDigitLength();

        long i = 0;
        long j = length - 1;
        while (i < j) {
            int d1 = digits.get(i), d2 = digits.get(j);
            if (d1 != d2) {
                return false;
            }
            i++;
            j--;
        }

        return true;
    }

    final static class Result {
        private final boolean isLychrelNumber;
        private final long iterationCount, digitLength;

        Result(boolean isLychrelNumber, long iterationCount, long digitLength) {
            this.isLychrelNumber = isLychrelNumber;
            this.iterationCount = iterationCount;
            this.digitLength = digitLength;
        }

        boolean isLychrelNumber() {
            return isLychrelNumber;
        }

        long getIterationCount() {
            return iterationCount;
        }

        long getDigitLength() {
            return digitLength;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).add("result", isLychrelNumber).add("iterationCount", iterationCount)
                    .add("digitCount", digitLength).toString();
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(isLychrelNumber, iterationCount, digitLength);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o.getClass() != this.getClass()) {
                return false;
            }
            Result that = (Result) o;
            return this.isLychrelNumber == that.isLychrelNumber & this.iterationCount == that.iterationCount
                    & this.digitLength == that.digitLength;
        }
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

        int addToDigitAt(long index, int toAdd) {
            int carry;

            int byteIndex = (int) (index >>> 1);
            int b = digits[byteIndex] & 0xFF;
            if ((index & 1) == 0) {
                // inlined get
                int digit = (b & LOW_MASK) + toAdd;
                if (digit >= 10) {
                    carry = digit / 10;
                    digit = digit % 10;
                } else {
                    carry = 0;
                }
                // inlined set
                digits[byteIndex] = (byte) ((b & HIGH_MASK) | digit);
            } else {
                // inlined get
                int digit = (b >>> 4) + toAdd;
                if (digit >= 10) {
                    carry = digit / 10;
                    digit = digit % 10;
                } else {
                    carry = 0;
                }
                // inlined set
                digits[byteIndex] = (byte) ((digit << 4) | (b & LOW_MASK));
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
