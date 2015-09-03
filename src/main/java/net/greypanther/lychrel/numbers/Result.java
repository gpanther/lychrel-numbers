package net.greypanther.lychrel.numbers;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

final class Result {
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