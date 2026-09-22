package ru.nsu.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CardTest {

    @Test
    void testGetValue() {
        Card card = new Card(Suit.SPADES, Rank.FIVE);
        assertEquals(5, card.getValue());
    }

    @Test
    void testIsAce() {
        Card ace = new Card(Suit.SPADES, Rank.ACE);
        Card king = new Card(Suit.SPADES, Rank.KING);
        assertTrue(ace.isAce());
        assertFalse(king.isAce());
    }
}