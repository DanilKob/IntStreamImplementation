package ua.procamp.streams.stream;

public interface IntIterator {
    boolean moveToNext();

    int getCurrentInt();

    int getCurrentIndex();

    void setCurrentIndex(int currentIndex);

    boolean isFinished();

    void add(int element);
}
