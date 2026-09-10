package ru.nsu.oop;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class mainTest {
    @Test
    void testSortTypical() {
        int[] arr = {5, 4, 3, 2, 1};
        Main.heapSort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void testSortAlreadySorted() {
        int[] arr = {1, 2, 3, 4, 5};
        Main.heapSort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void testSortReverse() {
        int[] arr = {9, 7, 5, 3, 1};
        Main.heapSort(arr);
        assertArrayEquals(new int[]{1, 3, 5, 7, 9}, arr);
    }

    @Test
    void testSortWithDuplicates() {
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6, 5};
        Main.heapSort(arr);
        assertArrayEquals(new int[]{1, 1, 2, 3, 4, 5, 5, 6, 9}, arr);
    }

    @Test
    void testSortEmpty() {
        int[] arr = {};
        Main.heapSort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void testSortSingle() {
        int[] arr = {42};
        Main.heapSort(arr);
        assertArrayEquals(new int[]{42}, arr);
    }


    @Test
    void testMain() {
        // покрывает весь код внутри main, включая цикл замера времени
        Main.main(new String[]{});
    }
}
