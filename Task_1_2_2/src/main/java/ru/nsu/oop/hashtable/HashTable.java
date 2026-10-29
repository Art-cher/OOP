package ru.nsu.oop.hashtable;

import java.util.ArrayList;
import java.util.List;

/**
 * Параметризованная хеш-таблица с разрешением коллизий методом цепочек.
 *
 * <p>Внутри хранится список бакетов {@code List<Entry<K, V>>}. Каждый элемент
 * этого списка — голова связного списка записей, попавших в один бакет.</p>
 *
 * @param <K> тип ключей
 * @param <V> тип значений
 */
public class HashTable<K, V> {

    /** Ёмкость по умолчанию — 16 бакетов. */
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    /** Список бакетов. {@code buckets.get(i)} — голова i-й цепочки. */
    private final List<Entry<K, V>> buckets;

    /** Количество пар ключ-значение в таблице. */
    private int size;

    /**
     * Создаёт пустую таблицу с ёмкостью по умолчанию.
     */
    public HashTable() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Создаёт пустую таблицу с заданной начальной ёмкостью.
     *
     * @param initialCapacity начальное количество бакетов
     * @throws IllegalArgumentException если {@code initialCapacity < 1}
     */
    public HashTable(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.buckets = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            buckets.add(null);
        }
        this.size = 0;
    }

    /**
     * @return количество пар ключ-значение в таблице
     */
    public int size() {
        return size;
    }

    /**
     * Запись внутри бакета. Хранит пару ключ-значение и ссылку на следующую
     * запись в цепочке.
     *
     * @param <K> тип ключа
     * @param <V> тип значения
     */
    public static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        /**
         * @return ключ записи
         */
        public K getKey() {
            return key;
        }

        /**
         * @return значение записи
         */
        public V getValue() {
            return value;
        }
    }
}