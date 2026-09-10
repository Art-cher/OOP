package ru.nsu.oop;
import java.util.Random;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void heapSort(int[] arr) {
        int n = arr.length;

        // Построение кучи (max-heap)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // Извлечение элементов из кучи
        for (int i = n - 1; i > 0; i--) {
            // Перемещаем корень в конец
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            // Восстанавливаем кучу для оставшейся части
            heapify(arr, i, 0);
        }
    }

    private static void heapify(int[] arr, int size, int root) {
        int largest = root;
        int left = 2 * root + 1;
        int right = 2 * root + 2;

        if (left < size && arr[left] > arr[largest]) {
            largest = left;
        }
        if (right < size && arr[right] > arr[largest]) {
            largest = right;
        }
        if (largest != root) {
            int swap = arr[root];
            arr[root] = arr[largest];
            arr[largest] = swap;
            heapify(arr, size, largest);
        }
    }
    public static void main(String[] args) {
        System.out.println("Демонстрация пирамидальной сортировки");

        // Пример 1: сортировка простого массива
        int[] testArray = {5, 4, 3, 2, 1};
        System.out.print("Исходный массив: ");
        printArray(testArray);
        heapSort(testArray);
        System.out.print("Отсортированный: ");
        printArray(testArray);

        // Пример 2: замер времени для разных размеров
        System.out.println("\nЗамер времени выполнения:");
        int[] sizes = {1000, 10000, 100000, 500000};
        for (int size : sizes) {
            int[] arr = generateRandomArray(size);
            long start = System.nanoTime();
            heapSort(arr);
            long duration = System.nanoTime() - start;
            System.out.printf("Размер %d -> %.2f мс%n", size, duration / 1_000_000.0);
        }
        System.out.printf("Hello and welcome!");


    }
    private static int[] generateRandomArray(int n) {
        Random rand = new Random();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = rand.nextInt(1000000);
        }
        return arr;
    }

    private static void printArray(int[] arr) {
        for (int x : arr) {
            System.out.print(x + " ");
        }
        System.out.println();
    }
}