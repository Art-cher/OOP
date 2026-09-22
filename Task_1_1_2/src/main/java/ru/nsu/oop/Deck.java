package ru.nsu.oop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
    private final List<Card> cards;
    private final Random random;
    private final int NUMBER_OD_DESKS = 8;

    public Deck() {
        this.cards = new ArrayList<>();
        this.random = new Random(); // или new Random(42) для тестов
        for (int i = 0; i < NUMBER_OD_DESKS; i++) {
            fillAndShuffle();
        }
    }

    private void fillAndShuffle() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(cards, random);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            reset();
        }
        return cards.removeLast();
    }

    private void reset() {
        cards.clear();
        fillAndShuffle();
    }
}