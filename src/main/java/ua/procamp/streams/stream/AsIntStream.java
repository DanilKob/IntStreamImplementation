package ua.procamp.streams.stream;

import ua.procamp.streams.function.*;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class AsIntStream implements IntStream {

    private int[] values;
    private int workLength;
    private IntIterator intIterator;

    private AsIntStream() {
        // To Do
    }

    public static IntStream of(int... values) {
        AsIntStream asIntStream = new AsIntStream();
        asIntStream.intIterator = asIntStream.new DefaultIntStreamIterator();
        asIntStream.values = asIntStream.initArray(values);
        asIntStream.workLength = values.length;
        return asIntStream;
    }

    @Override
    public Double average() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer max() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer min() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer sum() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void forEach(IntConsumer action) {
        while (intIterator.moveToNext()) {
            action.accept(intIterator.getCurrentInt());
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

        public boolean moveToNext() {
            ++currentInd;
            this.isFinished = currentInd >= values.length;
            return !this.isFinished;
        }

        public int getCurrentInt() {
            return values[currentInd];
        }

        public int getCurrentIndex() {
            return currentInd;
        }

        public void setCurrentIndex(int currentIndex) {
            this.currentInd = currentIndex;
        }

        public boolean isFinished() {
            return this.isFinished;
        }

        @Override
        public void add(int element) {
            int currentIndex = this.getCurrentIndex();
            if (currentIndex < values.length) {
                workLength = values.length +1;
                values = initArray(values);
            }
            values[workLength] = element;
        }
    }

    private int[] initArray(int[] oldValues) {
        int[] newArray = Arrays.copyOf(oldValues, (int) (oldValues.length * 1.5) + 1);
        return newArray;
    }

}
