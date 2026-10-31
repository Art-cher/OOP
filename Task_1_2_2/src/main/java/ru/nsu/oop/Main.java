package ru.nsu.oop;

import ru.nsu.oop.hashtable.HashTable;

/**
 * Демонстрация работы {@link HashTable}.
 */
public class Main {

    /**
     * Запускает демонстрацию: добавление, поиск, обновление, удаление,
     * итерация, resize, сравнение двух таблиц.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        System.out.println("=== Базовые операции ===");
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);
        System.out.println("Размер: " + table.size());
        System.out.println("Содержимое: " + table);
        System.out.println("get(\"two\") = " + table.get("two"));
        System.out.println("containsKey(\"four\") = " + table.containsKey("four"));

        System.out.println();
        System.out.println("=== Обновление и удаление ===");
        table.update("one", 100);
        System.out.println("После update(\"one\", 100): " + table.get("one"));
        Integer removed = table.remove("two");
        System.out.println("remove(\"two\") вернул: " + removed);
        System.out.println("Размер после remove: " + table.size());
        System.out.println("Содержимое: " + table);

        System.out.println();
        System.out.println("=== Итерация ===");
        for (HashTable.Entry<String, Integer> entry : table) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }

        System.out.println();
        System.out.println("=== Resize: 1000 элементов с начальной ёмкостью 2 ===");
        HashTable<Integer, Integer> big = new HashTable<>(2);
        for (int i = 0; i < 1000; i++) {
            big.put(i, i * 10);
        }
        System.out.println("Размер: " + big.size());
        System.out.println("get(0) = " + big.get(0));
        System.out.println("get(500) = " + big.get(500));
        System.out.println("get(999) = " + big.get(999));

        System.out.println();
        System.out.println("=== Сравнение таблиц ===");
        HashTable<String, Integer> a = new HashTable<>();
        a.put("x", 1);
        a.put("y", 2);

        HashTable<String, Integer> b = new HashTable<>(64);
        b.put("y", 2);
        b.put("x", 1);

        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("a.equals(b) = " + a.equals(b));
        System.out.println("a.hashCode() == b.hashCode() = "
                + (a.hashCode() == b.hashCode()));
    }
}