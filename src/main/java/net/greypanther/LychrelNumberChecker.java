package net.greypanther;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

final class LychrelNumberChecker {
    private LychrelNumberChecker() {
        throw new UnsupportedOperationException("Should never be instantiated");
    }

    static Result check(long seed, long maxIterationCount) {
        DigitStore digits1 = new DigitStore();
        DigitStore digits2 = new DigitStore();

        copy(seed, digits1);

        for (long i = 0; i < maxIterationCount; ++i) {
            if (isPalindrome(digits1)) {
                return new Result(true, i, digits1.getDigitLength());
            }

            if ((i & 1023) == 0) {
                System.out.format("Iteration %,d, digit length %,d\n", i, digits1.getDigitLength());
            }

            long length = digits1.getDigitLength();
            long k = length - 1;
            int carry = 0;

            for (long j = 0; j < length; ++j) {
                int d1 = digits1.get(j);
                int d2 = digits1.get(k--);

                int digit = d1 + d2 + carry;
                if (digit >= 10) {
                    carry = digit / 10;
                    digit = digit % 10;
                } else {
                    carry = 0;
                }
                digits2.set(j, digit);
            }

            if (carry > 0) {
                digits2.set(length, carry);
                digits2.setDigitLength(length + 1);
            } else {
                digits2.setDigitLength(length);
            }

            digits1.copyFrom(digits2);
        }

        return new Result(false, maxIterationCount, digits1.getDigitLength());
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

        private final byte[] digits = new byte[4096];
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

        void copyFrom(DigitStore other) {
            int byteCount = (int) (other.digitLength / 2);
            if (other.digitLength % 2 > 0) {
                byteCount += 1;
            }
            System.arraycopy(other.digits, 0, digits, 0, byteCount);
            digitLength = other.digitLength;
        }

        long getDigitLength() {
            return digitLength;
        }

        void setDigitLength(long digitLength) {
            this.digitLength = digitLength;
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
}
