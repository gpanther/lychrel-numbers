package net.greypanther.lychrel.numbers;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public final class Result {
    private final boolean cycleFound;
    private final long iterationCount, digitLength;

    Result(boolean cycleFound, long iterationCount, long digitLength) {
        this.cycleFound = cycleFound;
        this.iterationCount = iterationCount;
        this.digitLength = digitLength;
    }

    public boolean wasCycleFound() {
        return cycleFound;
    }

    public long getIterationCount() {
        return iterationCount;
    }

    public long getDigitLength() {
        return digitLength;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("result", cycleFound).add("iterationCount", iterationCount)
                .add("digitCount", digitLength).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cycleFound, iterationCount, digitLength);
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
        return this.cycleFound == that.cycleFound & this.iterationCount == that.iterationCount
                & this.digitLength == that.digitLength;
    }
}