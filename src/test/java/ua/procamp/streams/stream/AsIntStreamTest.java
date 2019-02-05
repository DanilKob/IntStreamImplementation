package ua.procamp.streams.stream;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;

import static org.junit.Assert.*;

public class AsIntStreamTest {

    private int[] values = {1, 2, 5, 8, 9, 10};
    private IntStream intStream;

    @Before
    public void initStream() {
        this.intStream = AsIntStream.of(values);
    }

    @Test
    public void of() {

        assertNotNull(intStream);

        List<Integer> integerList = new LinkedList<>();
        intStream.forEach(integerList::add);
        int[] intsArray = integerList
                .stream()
                .mapToInt(Integer::intValue)
                .toArray();
        assertArrayEquals(values, intsArray);
    }

    @Test
    public void average() {
        double sum = 0;
        int index = 0;
        for (int value : this.values) {
            sum += value;
            ++index;
        }

        Double average = intStream.average();
        assertEquals(average, sum / index, 0.01);
    }

    @Test
    public void max() {
        int actualMax = intStream.max();
        OptionalInt max = Arrays.stream(values).max();
        assertEquals(max.getAsInt(), actualMax);
    }

    @Test
    public void min() {
        int actualMin = intStream.min();
        OptionalInt min = Arrays.stream(values).min();
        assertEquals(min.getAsInt(), actualMin);
    }

    @Test
    public void count() {
        long actualCount = intStream.count();
        long count = values.length;
        assertEquals(count, actualCount);
    }

    @Test
    public void sum() {
        int sum = 0;
        for (int value : this.values) {
            sum += value;
        }

        int sumActual = intStream.sum();
        assertEquals(sum, sumActual);
    }

    @Test
    public void filter() {
        int[] array = Arrays
                .stream(values)
                .filter(value -> value > 5)
                .toArray();
        int[] actualArray = intStream
                .filter(value -> value > 5)
                .toArray();
        assertArrayEquals(array, actualArray);
    }

    @Test
    public void forEach() {
        List<Integer> integerList = new LinkedList<>();
        intStream.forEach(integerList::add);
        assertArrayEquals(integerList.stream().mapToInt(Integer::intValue).toArray(), values);
    }

    @Test
    public void map() {
        assertArrayEquals(
                intStream.map(value -> value * 2).toArray(),
                Arrays.stream(values).map(value -> value * 2).toArray()
        );
    }

    @Test
    public void flatMap() {
        assertArrayEquals(
                intStream.flatMap(value -> AsIntStream.of(value, value * 2)).toArray(),
                Arrays.stream(values).flatMap(value -> java.util.stream.IntStream.of(value, value*2)).toArray()
        );
    }

    @Test
    public void reduce() {
        assertEquals(
                intStream.reduce(0, (left, right) -> left + right),
                Arrays.stream(values).reduce(0, (left, right) -> left + right)
        );
    }

    @Test
    public void toArray() {
        assertArrayEquals(
                intStream.filter(value -> value > 4).map(value -> value *3).toArray(),
                Arrays.stream(values).filter(value -> value > 4).map(value -> value *3).toArray()
        );
    }
}