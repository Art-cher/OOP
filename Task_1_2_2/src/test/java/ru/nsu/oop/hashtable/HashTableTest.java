package ru.nsu.oop.hashtable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void put_newKey_increasesSizeAndAllowsGet() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);

        assertEquals(1, table.size());
        assertEquals(1, table.get("one"));
    }

    @Test
    void put_twoDifferentKeys_keepsBoth() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);

        assertEquals(2, table.size());
        assertEquals(1, table.get("one"));
        assertEquals(2, table.get("two"));
    }

    @Test
    void intMinValueTest() {
        HashTable<String, Integer> table = new HashTable<>();
        assertDoesNotThrow(() -> table.put("polygenelubricants", 42));
    }

    @Test
    void put_existingKey_throwsException() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);

        assertThrows(IllegalArgumentException.class, () -> table.put("one", 100));
        assertEquals(1, table.size());
        assertEquals(1, table.get("one"));
    }

    @Test
    void get_missingKey_returnsNull() {
        HashTable<String, Integer> table = new HashTable<>();
        assertNull(table.get("ghost"));
    }

    @Test
    void put_nullKey_works() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put(null, 42);

        assertEquals(1, table.size());
        assertEquals(42, table.get(null));
    }

    @Test
    void put_nullValue_works() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("x", null);

        assertEquals(1, table.size());
        assertNull(table.get("x"));
    }
}