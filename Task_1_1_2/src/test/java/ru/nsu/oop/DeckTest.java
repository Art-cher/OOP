package ru.nsu.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeckTest {

    @Test
    void testDrawCardReturnsValidCard() {
        Deck deck = new Deck();
        Card card = deck.drawCard();
        assertNotNull(card);
        assertNotNull(card.suit());
        assertNotNull(card.rank());
    }

    @Test
    void testDrawManyCards() {
        Deck deck = new Deck();
        // 8 колод * 52 карты = 416 карт
        for (int i = 0; i < 416; i++) {
            Card card = deck.drawCard();
            assertNotNull(card);
        }
        // После 416 карт колода должна пересоздаться
        Card card = deck.drawCard();
        assertNotNull(card);
    }
}