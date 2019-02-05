package ua.procamp.streams.stream;

import ua.procamp.streams.function.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AsIntStream implements IntStream {

    private int[] values;
    private int workLength;
    private IntIterator intIterator;

    private AsIntStream() {
    }

    public static IntStream of(int... values) {
        return of(true, values);
    }

    private static AsIntStream of(boolean needToExtendArray, int... values) {
        AsIntStream asIntStream = new AsIntStream();
        asIntStream.intIterator = asIntStream.new DefaultIntStreamIterator();
        asIntStream.workLength = values.length;
        asIntStream.values = needToExtendArray ? asIntStream.initExtendedArray(values) : values;
        return asIntStream;
    }

    @Override
    public Double average() {
        double sum = 0;
        int count = 0;
        while (intIterator.moveToNext()) {
            sum += intIterator.getCurrentInt();
            ++count;
        }

        return count == 0 ? sum : sum / count;
    }

    @Override
    public Integer max() {
        int currentInt;
        int max = Integer.MIN_VALUE;
        while (intIterator.moveToNext()) {
            currentInt = intIterator.getCurrentInt();
            if (currentInt > max) {
                max = currentInt;
            }
        }

        return max;
    }

    @Override
    public Integer min() {
        int currentInt;
        int min = Integer.MAX_VALUE;
        while (intIterator.moveToNext()) {
            currentInt = intIterator.getCurrentInt();
            if (currentInt < min) {
                min = currentInt;
            }
        }

        return min;
    }

    @Override
    public long count() {
        AtomicInteger count = new AtomicInteger();
        this.forEach(integer -> count.incrementAndGet());
        return count.get();
    }

    @Override
    public Integer sum() {
        AtomicInteger sum = new AtomicInteger();
        this.forEach(sum::addAndGet);
        return sum.get();
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        IntIterator oldIterator = this.intIterator;
        IntIterator intItr = new ProxyIntIterator(oldIterator) {
            @Override
            public boolean moveToNext() {
                while (oldIterator.moveToNext() && !predicate.test(oldIterator.getCurrentInt())) {

                }
                return !this.isFinished();
            }
        };
        this.setIterator(intItr);
        return this;
    }

    @Override
    public void forEach(IntConsumer action) {
        while (intIterator.moveToNext()) {
            action.accept(intIterator.getCurrentInt());
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        IntIterator oldIterator = this.intIterator;
        IntIterator intItr = new ProxyIntIterator(oldIterator) {
            @Override
            public int getCurrentInt() {
                return mapper.apply(super.getCurrentInt());
            }
        };
        this.setIterator(intItr);
        return this;
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        List<Integer> valueList = new LinkedList<>();
        IntStream intStream;
        int integer;
        while (intIterator.moveToNext()) {
            integer = intIterator.getCurrentInt();
            intStream = func.applyAsIntStream(integer);
            intStream.forEach(valueList::add);
        }

        int[] valuesAfterMap = integerListToArray(valueList);

        return AsIntStream.of(valuesAfterMap);
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        int right;
        int left = identity;
        while (intIterator.moveToNext()) {
            right = intIterator.getCurrentInt();
            left = op.apply(left, right);
        }
        return left;
    }

    @Override
    public int[] toArray() {
        return Arrays.copyOfRange(values, 0, workLength);
    }

    private void setIterator(IntIterator intIterator) {
        this.intIterator = intIterator;
    }

    private class DefaultIntStreamIterator implements IntIterator {
        private int currentInd = -1;
        private boolean isFinished = true;

        @Override
        public boolean moveToNext() {
            ++currentInd;
            this.isFinished = currentInd >= workLength;
            return !this.isFinished;
        }

        @Override
        public int getCurrentInt() {
            return values[currentInd];
        }

        @Override
        public int getCurrentIndex() {
            return currentInd;
        }

        @Override
        public void setCurrentIndex(int currentIndex) {
            this.currentInd = currentIndex;
        }

        @Override
        public boolean isFinished() {
            return this.isFinished;
        }

        @Override
        public void add(int element) {
            if (workLength >= values.length) {
                workLength = values.length;
                values = initExtendedArray(values);
            }
            values[workLength] = element;
            ++workLength;
        }
    }

    private int[] initExtendedArray(int[] oldValues) {
        int[] newArray = Arrays.copyOf(oldValues, (int) (oldValues.length * 1.5) + 1);
        return newArray;
    }

    private int[] integerListToArray(List<Integer> valueList) {
        int[] valuesAfterMap = new int[valueList.size()];
        int index = -1;
        for (Integer integ : valueList) {
            valuesAfterMap[++index] = integ;
        }
        return valuesAfterMap;
    }
}
