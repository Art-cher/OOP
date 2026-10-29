package ru.nsu.oop.hashtable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Параметризованная хеш-таблица с разрешением коллизий методом цепочек.
 *
 * <p>Внутри хранится список бакетов {@code List<Entry<K, V>>}. Каждый элемент
 * этого списка — голова связного списка записей, попавших в один бакет.</p>
 *
 * <p>При превышении размера {@code size > capacity * loadFactor} таблица
 * автоматически расширяется вдвое, и все записи перехешируются в новую
 * таблицу. Это сохраняет амортизированное O(1) на {@code put}/{@code get}.</p>
 *
 * @param <K> тип ключей
 * @param <V> тип значений
 */
public class HashTable<K, V> {

    /** Ёмкость по умолчанию — 16 бакетов. */
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    /** Порог заполнения — при превышении происходит resize. */
    private static final double LOAD_FACTOR = 0.75;

    /** Список бакетов. {@code buckets.get(i)} — голова i-й цепочки. */
    private List<Entry<K, V>> buckets;

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
     * возвращает количество пар в таблице.
     *
     * @return количество пар ключ-значение в таблице
     */
    public int size() {
        return size;
    }

    /**
     * Добавляет новую пару ключ-значение в таблицу.
     *
     * @param key   ключ
     * @param value значение
     * @throws IllegalArgumentException если ключ уже есть в таблице
     */
    public void put(K key, V value) {
        if (findEntry(key) != null) {
            throw new IllegalArgumentException("Key already exists: " + key);
        }
        putEntry(key, value, buckets, buckets.size());
        size++;

        if (size > buckets.size() * LOAD_FACTOR) {
            resize();
        }
    }

    /**
     * Возвращает значение по ключу или {@code null}, если ключа нет.
     *
     * @param key ключ
     * @return значение или {@code null}
     */
    public V get(K key) {
        Entry<K, V> entry = findEntry(key);
        return entry == null ? null : entry.value;
    }

    /**
     * Проверяет наличие ключа в таблице. Работает и для ключей со значением
     * {@code null}.
     *
     * @param key ключ
     * @return {@code true}, если ключ присутствует
     */
    public boolean containsKey(K key) {
        return findEntry(key) != null;
    }

    /**
     * Обновляет значение по существующему ключу.
     *
     * @param key   ключ
     * @param value новое значение
     * @throws IllegalArgumentException если ключа нет в таблице
     */
    public void update(K key, V value) {
        Entry<K, V> entry = findEntry(key);
        if (entry == null) {
            throw new IllegalArgumentException("Key not found: " + key);
        }
        entry.value = value;
    }

    /**
     * Удаляет пару по ключу и возвращает её значение. Если ключа нет —
     * возвращает {@code null}, не бросает исключение.
     *
     * @param key ключ
     * @return удалённое значение или {@code null}
     */
    public V remove(K key) {
        int index = hash(key);
        Entry<K, V> current = buckets.get(index);
        Entry<K, V> prev = null;

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                if (prev == null) {
                    buckets.set(index, current.next);
                } else {
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    /**
     * Возвращает текущее количество бакетов. Package-private — используется
     * только в тестах для проверки логики resize, не часть публичного API.
     *
     * @return текущая ёмкость таблицы
     */
    int capacity() {
        return buckets.size();
    }

    /**
     * Вычисляет индекс бакета для заданного ключа.
     *
     * @param key ключ
     * @return индекс в диапазоне {@code [0, buckets.size())}
     */
    private int hash(K key) {
        return hash(key, buckets.size());
    }

    /**
     * Вычисляет индекс бакета для заданного ключа при заданной ёмкости.
     * Ёмкость передаётся явно — это нужно в {@link #resize()}, где индекс
     * считается для нового массива, ещё не присвоенного {@code buckets}.
     *
     * @param key      ключ
     * @param capacity размер массива бакетов
     * @return индекс в диапазоне {@code [0, capacity)}
     */
    private int hash(K key, int capacity) {
        return (Objects.hashCode(key) & 0x7fffffff) % capacity;
    }

    /**
     * Находит запись по ключу в соответствующем бакете. Возвращает {@code null},
     * если записи нет.
     *
     * @param key ключ
     * @return запись или {@code null}
     */
    private Entry<K, V> findEntry(K key) {
        int index = hash(key);
        Entry<K, V> current = buckets.get(index);

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Расширяет таблицу вдвое и перехеширует все записи.
     *
     * <p>Порядок внутри бакета меняется: записи переносятся в голову новой
     * цепочки. Для семантики это не важно — {@code findEntry} обходит всю
     * цепочку, а не только голову.</p>
     */
    private void resize() {
        List<Entry<K, V>> oldBuckets = buckets;
        int newCapacity = oldBuckets.size() * 2;

        List<Entry<K, V>> newBuckets = new ArrayList<>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            newBuckets.add(null);
        }

        for (Entry<K, V> head : oldBuckets) {
            Entry<K, V> current = head;
            while (current != null) {
                putEntry(current.key, current.value, newBuckets, newCapacity);
                current = current.next;
            }
        }

        buckets = newBuckets;
    }

    /**
     * Вставляет пару в целевой массив бакетов. Используется и в
     * {@link #put(Object, Object)}, и в {@link #resize()}: в обоих случаях
     * нужно положить запись в голову цепочки нужного бакета.
     *
     * <p>Не проверяет дубликаты, не трогает {@code size}, не проверяет порог —
     * это ответственность вызывающего.</p>
     *
     * @param key            ключ
     * @param value          значение
     * @param targetBuckets  массив бакетов, в который идёт вставка
     * @param targetCapacity размер {@code targetBuckets}
     */
    private void putEntry(K key, V value, List<Entry<K, V>> targetBuckets, int targetCapacity) {
        int index = hash(key, targetCapacity);
        targetBuckets.set(index, new Entry<>(key, value, targetBuckets.get(index)));
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
         * возвращает ключ записи.
         *
         * @return ключ записи
         */
        public K getKey() {
            return key;
        }

        /**
         * возвращвет значение записи.
         *
         * @return значение записи
         */
        public V getValue() {
            return value;
        }
    }
}