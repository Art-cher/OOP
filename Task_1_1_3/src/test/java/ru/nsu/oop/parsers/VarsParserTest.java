package ru.nsu.oop.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Тесты для {@link VarsParser}.
 */
class VarsParserTest {

    @Test
    void parseSingleVar() {
        Map<String, Integer> map = VarsParser.parseVars("x = 10");
        assertEquals(1, map.size());
        assertEquals(10, map.get("x"));
    }

    @Test
    void parseMultipleVars() {
        Map<String, Integer> map = VarsParser.parseVars("x = 10; y = 13");
        assertEquals(2, map.size());
        assertEquals(10, map.get("x"));
        assertEquals(13, map.get("y"));
    }

    @Test
    void parseWithExtraSpaces() {
        Map<String, Integer> map = VarsParser.parseVars("  x   =   42  ");
        assertEquals(42, map.get("x"));
    }

    @Test
    void parseNegativeValue() {
        Map<String, Integer> map = VarsParser.parseVars("x = -5");
        assertEquals(-5, map.get("x"));
    }

    @Test
    void parseMultiLetterVariable() {
        Map<String, Integer> map = VarsParser.parseVars("alpha = 1; beta = 2");
        assertEquals(1, map.get("alpha"));
        assertEquals(2, map.get("beta"));
    }

    @Test
    void parseNullReturnsEmpty() {
        assertTrue(VarsParser.parseVars(null).isEmpty());
    }

    @Test
    void parseBlankReturnsEmpty() {
        assertTrue(VarsParser.parseVars("   ").isEmpty());
    }

    @Test
    void parseEmptyReturnsEmpty() {
        assertTrue(VarsParser.parseVars("").isEmpty());
    }

    @Test
    void parseMissingEqualsThrows() {
        assertThrows(IllegalArgumentException.class, () -> VarsParser.parseVars("x 10"));
    }

    @Test
    void parseEmptyVariableNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> VarsParser.parseVars("= 10"));
    }

    @Test
    void parseNonIntegerValueThrows() {
        assertThrows(IllegalArgumentException.class, () -> VarsParser.parseVars("x = abc"));
    }
}