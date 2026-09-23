package ru.nsu.oop.game;

import ru.nsu.oop.cards.Card;
import ru.nsu.oop.cards.Deck;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

/**
 * Колода с заранее заданной последовательностью карт.
 * Используется в тестах, чтобы получить детерминированную раздачу.
 */
class TestDeck extends Deck {

    private final Queue<Card> cards = new ArrayDeque<>();

    /** Кладёт несколько карт сразу. */
    void addCards(Card... cardsToAdd) {
        Collections.addAll(cards, cardsToAdd);
    }

    @Override
    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("TestDeck is empty");
        }
        return cards.poll();
    }
}