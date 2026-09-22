package ru.nsu.oop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DealerTest {

    @Test
    void testGetHiddenCardIndex() {
        Dealer dealer = new Dealer();
        assertEquals(1, dealer.getHiddenCardIndex());
    }

    @Test
    void testShouldDraw() {
        Dealer dealer = new Dealer();
        // Пустая рука — сумма 0, должен брать
        assertTrue(dealer.shouldDraw());

        dealer.addCard(new Card(Suit.SPADES, Rank.TEN));
        // Сумма 10, должен брать
        assertTrue(dealer.shouldDraw());

        dealer.addCard(new Card(Suit.HEARTS, Rank.SEVEN));
        // Сумма 17, не должен брать
        assertFalse(dealer.shouldDraw());

        dealer.resetHand();
        dealer.addCard(new Card(Suit.SPADES, Rank.ACE));
        dealer.addCard(new Card(Suit.HEARTS, Rank.SIX));
        // Сумма 17 (Ace как 11 + 6 = 17), не должен брать
        assertFalse(dealer.shouldDraw());
    }
}