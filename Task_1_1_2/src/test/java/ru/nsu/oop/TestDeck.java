package ru.nsu.oop;

import java.util.ArrayDeque;
import java.util.Queue;

class TestDeck extends Deck {
    private final Queue<Card> cards = new ArrayDeque<>();

    public void addCard(Card card) {
        cards.add(card);
    }

    @Override
    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No more cards in test deck");
        }
        return cards.poll();
    }
}