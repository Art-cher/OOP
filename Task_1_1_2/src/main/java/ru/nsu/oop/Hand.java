package ru.nsu.oop;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int getSum() {
        int baseSum = 0;
        for (Card c : cards) {
            baseSum += c.getValue();
        }
        return baseSum - countReducedAces() * 10;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && getSum() == 21;
    }

    public boolean isBust() {
        return getSum() > 21;
    }

    public void clear() {
        cards.clear();
    }

    /**
     * Возвращает количество карт в руке.
     */
    public int size() {
        return cards.size();
    }

    /**
     * Возвращает значение карты для отображения с учётом понижения тузов.
     * Например, если рука [Туз, 9, 9], то getSum() == 19,
     * а getDisplayValue(0) == 1, потому что туз понижен.
     *
     * @param index позиция карты
     */
    public int getDisplayValue(int index) {
        Card card = cards.get(index);
        if (!card.isAce()) {
            return card.getValue();
        }

        // Считаем, сколько тузов понижено в этой руке
        int reducedAces = countReducedAces();

        // Считаем, какой по счёту туз перед нами (0 — первый, 1 — второй...)
        int aceOrder = 0;
        for (int i = 0; i < index; i++) {
            if (cards.get(i).isAce()) {
                aceOrder++;
            }
        }

        // Первые reducedAces тузов понижены до 1, остальные — 11
        return aceOrder < reducedAces ? 1 : 11;
    }

    /** Считает, сколько тузов понижено, чтобы сумма помещалась в 21. */
    private int countReducedAces() {
        int baseSum = 0;
        int aces = 0;
        for (Card c : cards) {
            baseSum += c.getValue();
            if (c.isAce()) aces++;
        }
        int reduced = 0;
        int sum = baseSum;
        while (sum > 21 && reduced < aces) {
            sum -= 10;
            reduced++;
        }
        return reduced;
    }

    /**
     * Возвращает карту по индексу.
     *
     * @param index позиция карты, от 0 до size() - 1
     * @return карта на указанной позиции
     */
    public Card getCard(int index) {
        return cards.get(index);
    }
}