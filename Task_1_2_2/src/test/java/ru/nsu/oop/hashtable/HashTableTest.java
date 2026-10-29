package ru.nsu.oop.hashtable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class HashTableTest {

    @Test
    void newTable_hasSizeZero() {
        HashTable<String, Integer> table = new HashTable<>();
        assertEquals(0, table.size());
    }

    @Test
    void newTableWithCustomCapacity_hasSizeZero() {
        HashTable<String, Integer> table = new HashTable<>(4);
        assertEquals(0, table.size());
    }

    @Test
    void constructorWithZeroCapacity_throws() {
        assertThrows(IllegalArgumentException.class, () -> new HashTable<>(0));
    }

    @Test
    void constructorWithNegativeCapacity_throws() {
        assertThrows(IllegalArgumentException.class, () -> new HashTable<>(-5));
    }
}