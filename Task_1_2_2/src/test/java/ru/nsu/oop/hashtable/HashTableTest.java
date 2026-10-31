package ru.nsu.oop.hashtable;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
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
    void resize_triggeredWhenThresholdExceeded() {
        HashTable<String, Integer> table = new HashTable<>(4);
        assertEquals(4, table.capacity());

        table.put("a", 1);
        table.put("b", 2);
        table.put("c", 3);
        assertEquals(4, table.capacity());   // size=3, порог=3, ещё не превышен

        table.put("d", 4);                   // size=4 > 3 → resize
        assertEquals(8, table.capacity());
    }

    @Test
    void resize_preservesAllEntries() {
        HashTable<String, Integer> table = new HashTable<>(2);
        for (int i = 0; i < 100; i++) {
            table.put("key" + i, i);
        }

        assertEquals(100, table.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(i, table.get("key" + i));
        }
    }

    @Test
    void operationsAfterResize_work() {
        HashTable<String, Integer> table = new HashTable<>(2);
        for (int i = 0; i < 50; i++) {
            table.put("key" + i, i);
        }

        // после нескольких resize'ов
        table.update("key5", 500);
        assertEquals(500, table.get("key5"));

        table.remove("key10");
        assertNull(table.get("key10"));
        assertFalse(table.containsKey("key10"));
        assertEquals(49, table.size());
    }

    @Test
    void multipleResizes_allDataAccessible() {
        HashTable<Integer, Integer> table = new HashTable<>(2);
        for (int i = 0; i < 1000; i++) {
            table.put(i, i * 10);
        }

        assertEquals(1000, table.size());
        for (int i = 0; i < 1000; i++) {
            assertEquals(i * 10, table.get(i));
        }
    }

    @Test
    void iterator_emptyTable_noElements() {
        HashTable<String, Integer> table = new HashTable<>();
        Iterator<HashTable.Entry<String, Integer>> it = table.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    void iterator_singleEntry_returnsIt() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);

        Iterator<HashTable.Entry<String, Integer>> it = table.iterator();
        assertTrue(it.hasNext());
        HashTable.Entry<String, Integer> entry = it.next();
        assertEquals("one", entry.getKey());
        assertEquals(1, entry.getValue());
        assertFalse(it.hasNext());
    }

    @Test
    void iterator_multipleEntries_returnsAll() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);

        List<String> collected = new ArrayList<>();
        for (HashTable.Entry<String, Integer> entry : table) {
            collected.add(entry.getKey() + "=" + entry.getValue());
        }

        Collections.sort(collected);
        assertEquals(List.of("one=1", "three=3", "two=2"), collected);
    }

    @Test
    void iterator_withCollisions_returnsAll() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Aa", 1);
        table.put("BB", 2);

        List<String> collected = new ArrayList<>();
        for (HashTable.Entry<String, Integer> entry : table) {
            collected.add(entry.getKey() + "=" + entry.getValue());
        }

        Collections.sort(collected);
        assertEquals(List.of("Aa=1", "BB=2"), collected);
    }

    @Test
    void iterator_nextAfterExhaustion_throws() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);

        Iterator<HashTable.Entry<String, Integer>> it = table.iterator();
        it.next();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iterator_putDuringIteration_throwsCME() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);

        assertThrows(ConcurrentModificationException.class, () -> {
            for (HashTable.Entry<String, Integer> entry : table) {
                table.put("three", 3);
            }
        });
    }

    @Test
    void iterator_removeDuringIteration_throwsCME() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);

        assertThrows(ConcurrentModificationException.class, () -> {
            for (HashTable.Entry<String, Integer> entry : table) {
                table.remove("one");
            }
        });
    }

    @Test
    void iterator_updateDuringIteration_doesNotThrow() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);

        int count = 0;
        for (HashTable.Entry<String, Integer> entry : table) {
            table.update(entry.getKey(), 100);
            count++;
        }
        assertEquals(2, count);
        assertEquals(100, table.get("one"));
        assertEquals(100, table.get("two"));
    }

    @Test
    void equals_sameContentDifferentOrder_true() {
        HashTable<String, Integer> a = new HashTable<>();
        a.put("one", 1);
        a.put("two", 2);

        HashTable<String, Integer> b = new HashTable<>();
        b.put("two", 2);
        b.put("one", 1);

        assertEquals(a, b);
        assertEquals(b, a);  // симметричность
    }

    @Test
    void equals_sameContentDifferentCapacity_true() {
        HashTable<String, Integer> a = new HashTable<>(2);
        a.put("one", 1);
        a.put("two", 2);

        // b пройдёт resize'ы и получит другой capacity
        HashTable<String, Integer> b = new HashTable<>(2);
        for (int i = 0; i < 100; i++) {
            b.put("k" + i, i);
        }
        b.remove("k0");
        b.remove("k1");
        for (int i = 2; i < 100; i++) {
            b.remove("k" + i);
        }
        b.put("one", 1);
        b.put("two", 2);

        assertEquals(2, a.size());
        assertEquals(2, b.size());
        assertEquals(a, b);
    }

    @Test
    void equals_differentValue_false() {
        HashTable<String, Integer> a = new HashTable<>();
        a.put("one", 1);

        HashTable<String, Integer> b = new HashTable<>();
        b.put("one", 999);

        assertNotEquals(a, b);
    }

    @Test
    void equals_differentSize_false() {
        HashTable<String, Integer> a = new HashTable<>();
        a.put("one", 1);

        HashTable<String, Integer> b = new HashTable<>();
        b.put("one", 1);
        b.put("two", 2);

        assertNotEquals(a, b);
    }

    @Test
    void equals_differentKeys_false() {
        HashTable<String, Integer> a = new HashTable<>();
        a.put("one", 1);

        HashTable<String, Integer> b = new HashTable<>();
        b.put("uno", 1);

        assertNotEquals(a, b);
    }

    @Test
    void equals_null_false() {
        HashTable<String, Integer> a = new HashTable<>();
        assertNotEquals(null, a);
    }

    @Test
    void equals_otherType_false() {
        HashTable<String, Integer> a = new HashTable<>();
        assertNotEquals("not a table", a);
    }

    @Test
    void equals_nullValues_comparedCorrectly() {
        HashTable<String, Integer> a = new HashTable<>();
        a.put("x", null);

        HashTable<String, Integer> b = new HashTable<>();
        b.put("x", null);

        assertEquals(a, b);
    }

    @Test
    void hashCode_equalTables_sameHash() {
        HashTable<String, Integer> a = new HashTable<>();
        a.put("one", 1);
        a.put("two", 2);

        HashTable<String, Integer> b = new HashTable<>();
        b.put("two", 2);
        b.put("one", 1);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_equalTablesDifferentCapacity_sameHash() {
        HashTable<String, Integer> a = new HashTable<>(2);
        a.put("one", 1);
        a.put("two", 2);

        HashTable<String, Integer> b = new HashTable<>(2);
        for (int i = 0; i < 100; i++) {
            b.put("k" + i, i);
        }
        for (int i = 0; i < 100; i++) {
            b.remove("k" + i);
        }
        b.put("one", 1);
        b.put("two", 2);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void toString_empty_returnsBraces() {
        HashTable<String, Integer> table = new HashTable<>();
        assertEquals("{}", table.toString());
    }

    @Test
    void toString_withEntries_containsPairs() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);

        String s = table.toString();
        assertTrue(s.startsWith("{"));
        assertTrue(s.endsWith("}"));
        assertTrue(s.contains("one=1"));
        assertTrue(s.contains("two=2"));
    }

    @Test
    void toString_nullValue_printsNull() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("x", null);

        assertEquals("{x=null}", table.toString());
    }

    @Test
    void equals_sameKeyOneValueOneNull_false() {
        HashTable<String, Integer> a = new HashTable<>();
        a.put("x", 1);

        HashTable<String, Integer> b = new HashTable<>();
        b.put("x", null);

        assertNotEquals(a, b);
        assertNotEquals(b, a);   // симметричность
    }

    @Test
    void equals_bothNullAtDifferentKeys_false() {
        HashTable<String, Integer> a = new HashTable<>();
        a.put("x", null);

        HashTable<String, Integer> b = new HashTable<>();
        b.put("y", null);

        assertNotEquals(a, b);
    }

    @Test
    void equals_nullValueDoesNotMaskMissingKey_false() {
        // Ключевой тест: если в equals убрать containsKey, этот случай пройдёт
        // как равный, хотя таблицы разные.
        HashTable<String, Integer> a = new HashTable<>();
        a.put("x", null);
        a.put("y", 1);

        HashTable<String, Integer> b = new HashTable<>();
        b.put("y", 1);
        b.put("z", 2);

        assertNotEquals(a, b);
    }
}