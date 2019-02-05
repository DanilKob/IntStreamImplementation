package ua.procamp.streams.stream;

public class ProxyIntIterator implements IntIterator {

    private IntIterator oldIterator;

    public ProxyIntIterator (IntIterator oldIterator) {
        this.oldIterator = oldIterator;
    }

    @Override
    public boolean moveToNext() {
        return oldIterator.moveToNext();
    }

    @Override
    public int getCurrentInt() {
        return oldIterator.getCurrentInt();
    }

    @Override
    public int getCurrentIndex() {
        return oldIterator.getCurrentIndex();
    }

    @Override
    public void setCurrentIndex(int currentIndex) {
        oldIterator.setCurrentIndex(currentIndex);
    }

    @Override
    public boolean isFinished() {
        return oldIterator.isFinished();
    }

    @Override
    public void add(int element) {
        oldIterator.add(element);
    }
}
