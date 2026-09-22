package ru.nsu.oop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void testInitialState() {
        Player player = new Player();
        assertNotNull(player.getHand());
        assertEquals(0, player.getWins());
        assertFalse(player.isBlackjack());
        assertFalse(player.isBust());
    }

    @Test
    void testAddCardAndReset() {
        Player player = new Player();
        Card card = new Card(Suit.SPADES, Rank.TWO);
        player.addCard(card);
        assertEquals(1, player.getHand().size());
        player.resetHand();
        assertEquals(0, player.getHand().size());
    }

    @Test
    void testIncrementWins() {
        Player player = new Player();
        player.incrementWins();
        assertEquals(1, player.getWins());
        player.incrementWins();
        assertEquals(2, player.getWins());
    }

    @Test
    void testIsBlackjackAndBust() {
        Player player = new Player();
        player.addCard(new Card(Suit.SPADES, Rank.ACE));
        player.addCard(new Card(Suit.HEARTS, Rank.KING));
        assertTrue(player.isBlackjack());
        assertFalse(player.isBust());

        player.addCard(new Card(Suit.DIAMONDS, Rank.TWO));
        assertFalse(player.isBlackjack());
        assertFalse(player.isBust()); // сумма 13

        player.addCard(new Card(Suit.CLUBS, Rank.KING));
        // Ace(1) + King(10) + Two(2) + King(10) = 23
        assertTrue(player.isBust());
    }
}