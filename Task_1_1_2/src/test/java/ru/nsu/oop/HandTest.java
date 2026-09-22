package ru.nsu.oop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class HandTest {

    @Test
    void testEmptyHand() {
        Hand hand = new Hand();
        assertEquals(0, hand.size());
        assertEquals(0, hand.getSum());
        assertFalse(hand.isBlackjack());
        assertFalse(hand.isBust());
    }

    @Test
    void testAddCardAndGetCard() {
        Hand hand = new Hand();
        Card card = new Card(Suit.SPADES, Rank.TWO);
        hand.addCard(card);
        assertEquals(1, hand.size());
        assertEquals(card, hand.getCard(0));
    }

    @Test
    void testGetSumNoAces() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.SPADES, Rank.TWO));
        hand.addCard(new Card(Suit.HEARTS, Rank.THREE));
        assertEquals(5, hand.getSum());
    }

    @Test
    void testGetSumWithAceAsEleven() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addCard(new Card(Suit.HEARTS, Rank.NINE));
        assertEquals(20, hand.getSum());
        assertEquals(11, hand.getDisplayValue(0));
    }

    @Test
    void testGetSumWithAceReduced() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addCard(new Card(Suit.HEARTS, Rank.NINE));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.NINE));
        assertEquals(19, hand.getSum());
        assertEquals(1, hand.getDisplayValue(0)); // Ace reduced to 1
        assertEquals(9, hand.getDisplayValue(1));
        assertEquals(9, hand.getDisplayValue(2));
    }

    @Test
    void testMultipleAces() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        assertEquals(12, hand.getSum()); // 1 + 11 = 12
        assertEquals(1, hand.getDisplayValue(0));
        assertEquals(11, hand.getDisplayValue(1));

        hand.addCard(new Card(Suit.DIAMONDS, Rank.ACE));
        assertEquals(13, hand.getSum()); // 1 + 1 + 11 = 13
        assertEquals(1, hand.getDisplayValue(0));
        assertEquals(1, hand.getDisplayValue(1));
        assertEquals(11, hand.getDisplayValue(2));
    }

    @Test
    void testBlackjack() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        assertTrue(hand.isBlackjack());
        assertEquals(21, hand.getSum());

        // 21 с тремя картами не является блэкджеком
        hand.addCard(new Card(Suit.DIAMONDS, Rank.TWO));
        assertFalse(hand.isBlackjack());
        assertEquals(13, hand.getSum()); // Ace reduced: 1 + 10 + 2 = 13
    }

    @Test
    void testBust() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.SPADES, Rank.KING));
        hand.addCard(new Card(Suit.HEARTS, Rank.QUEEN));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.TWO));
        assertEquals(22, hand.getSum());
        assertTrue(hand.isBust());
    }

    @Test
    void testClear() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.SPADES, Rank.TWO));
        hand.clear();
        assertEquals(0, hand.size());
        assertEquals(0, hand.getSum());
    }
}