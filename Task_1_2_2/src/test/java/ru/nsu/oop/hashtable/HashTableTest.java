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

    @Test
    void containsKey_existingKey_returnsTrue() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        assertTrue(table.containsKey("one"));
    }

    @Test
    void containsKey_missingKey_returnsFalse() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        assertFalse(table.containsKey("two"));
    }

    @Test
    void containsKey_withNullValue_returnsTrue() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", null);
        assertTrue(table.containsKey("one"));
    }

    @Test
    void update_existingKey_changesValue() {
        HashTable<String, Number> table = new HashTable<>();
        table.put("one", 1);

        assertEquals(1, table.get("one"));
        assertEquals(1, table.size());

        table.update("one", 1.0);

        assertEquals(1.0, table.get("one"));
        assertEquals(1, table.size());
    }

    @Test
    void update_missingKey_throwsException() {
        HashTable<String, Integer> table = new HashTable<>();
        assertThrows(IllegalArgumentException.class, () -> table.update("ghost", 1));
    }

    @Test
    void update_toNull_works() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.update("one", null);

        assertNull(table.get("one"));
        assertTrue(table.containsKey("one"));
    }

    @Test
    void remove_existingKey_returnsValueAndDecreasesSize() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);


        assertEquals(2, table.size());

        Integer removed = table.remove("one");

        assertEquals(1, removed);
        assertEquals(1, table.size());
        assertNull(table.get("one"));
        assertFalse(table.containsKey("one"));
        assertEquals(2, table.get("two"));
    }

    @Test
    void remove_missingKey_returnsNull() {
        HashTable<String, Integer> table = new HashTable<>();
        assertNull(table.remove("ghost"));
        assertEquals(0, table.size());
    }

    @Test
    void remove_fromMiddleOfChain_works() {
        // Ёмкость 1 — все ключи попадают в один бакет, цепочка длинная
        HashTable<String, Integer> table = new HashTable<>(1);
        table.put("a", 1);
        table.put("b", 2);
        table.put("c", 3);

        Integer removed = table.remove("b");

        assertEquals(2, removed);
        assertEquals(2, table.size());
        assertNull(table.get("b"));
        assertEquals(1, table.get("a"));
        assertEquals(3, table.get("c"));
    }

    @Test
    void collisionPremise_aAndBB_HaveSameHashCode() {
        // Предпосылка для всех тестов ниже.
        // Если этот тест упадёт — коллизионные тесты ниже проверяют уже не то.
        assertEquals("Aa".hashCode(), "BB".hashCode());
    }

    @Test
    void put_collidingKeys_bothAccessible() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Aa", 1);
        table.put("BB", 2);   // тот же hashCode, тот же бакет

        assertEquals(2, table.size());
        assertEquals(1, table.get("Aa"));
        assertEquals(2, table.get("BB"));
    }

    @Test
    void put_collidingKeys_detectsDuplicate() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Aa", 1);
        table.put("BB", 2);

        // "Aa" уже есть, и он в цепочке — либо первый, либо второй, но findEntry
        // должен пройти всю цепочку и найти его в любом случае
        assertThrows(IllegalArgumentException.class, () -> table.put("Aa", 100));
        assertEquals(2, table.size());
    }

    @Test
    void containsKey_collidingKeys_distinguishesThem() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Aa", 1);

        assertTrue(table.containsKey("Aa"));
        assertFalse(table.containsKey("BB"));  // тот же бакет, другой ключ
    }

    @Test
    void update_collidingKeys_changesOnlyTarget() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Aa", 1);
        table.put("BB", 2);

        table.update("Aa", 100);

        assertEquals(100, table.get("Aa"));
        assertEquals(2, table.get("BB"));   // не тронут
    }

    @Test
    void remove_collidingKeys_removesOnlyTarget() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Aa", 1);
        table.put("BB", 2);

        Integer removed = table.remove("Aa");

        assertEquals(1, removed);
        assertEquals(1, table.size());
        assertFalse(table.containsKey("Aa"));
        assertEquals(2, table.get("BB"));   // остался
    }

    @Test
    void remove_firstInChain_collidingKeys_leavesRestIntact() {
        // При capacity=1 все ключи в одном бакете, порядок цепочки — порядок вставки
        // (в голову): последний вставленный — первый в цепочке
        HashTable<String, Integer> table = new HashTable<>(1);
        table.put("Aa", 1);
        table.put("BB", 2);

        // "BB" вставлен вторым → он в голове цепочки.
        // remove("BB") должен зайти в ветку prev == null (удаление головы).
        Integer removed = table.remove("BB");

        assertEquals(2, removed);
        assertEquals(1, table.get("Aa"));
    }
}