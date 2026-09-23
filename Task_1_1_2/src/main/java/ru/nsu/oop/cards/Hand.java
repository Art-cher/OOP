package ru.nsu.oop.cards;

import java.util.ArrayList;
import java.util.List;

/**
 * Рука участника: набор карт с подсчётом суммы и обработкой тузов.
 * Туз считается как 11 до тех пор, пока это не приводит к перебору;
 * иначе он понижается до 1.
 */
public class Hand {
    private final List<Card> cards;
    private int sum;
    private int reducedAces;

    /**
     * Создаёт пустую руку.
     */
    public Hand() {
        this.cards = new ArrayList<>();
        this.sum = 0;
        this.reducedAces = 0;
    }

    /**
     * Добавляет карту в руку.
     *
     * @param card карта для добавления
     */
    public void addCard(Card card) {
        cards.add(card);
        recalculate();
    }

    /**
     * Возвращает сумму карт.
     *
     * @return сумма карт
     */
    public int getSum() {
        return sum;
    }

    /**
     * Очищает руку.
     */
    public void clear() {
        cards.clear();
        this.sum = 0;
        this.reducedAces = 0;
    }

    /**
     * Возвращает количество карт в руке.
     *
     * @return число карт
     */
    public int size() {
        return cards.size();
    }

    /**
     * Возвращает значение карты для отображения с учётом понижения тузов.
     * Например, если рука [Туз, 9, 9], то {@code getSum() == 19},
     * а {@code getDisplayValue(0) == 1}, потому что туз понижен.
     *
     * @param index позиция карты
     * @return значение карты для отображения
     */
    public int getDisplayValue(int index) {
        Card card = cards.get(index);
        if (!card.isAce()) {
            return card.getValue();
        }

        int aceOrder = 0;
        for (int i = 0; i < index; i++) {
            if (cards.get(i).isAce()) {
                aceOrder++;
            }
        }

        return aceOrder < this.reducedAces ? 1 : 11;
    }

    /**
     * Возвращает карту по индексу.
     *
     * @param index позиция карты, от 0 до {@code size() - 1}
     * @return карта на указанной позиции
     */
    public Card getCard(int index) {
        return cards.get(index);
    }

    private void recalculate() {
        int baseSum = 0;
        int aces = 0;
        for (Card c : cards) {
            baseSum += c.getValue();
            if (c.isAce()) {
                aces++;
            }
        }
        int reduced = 0;
        int s = baseSum;
        while (s > 21 && reduced < aces) {
            s -= 10;
            reduced++;
        }
        this.sum = s;
        this.reducedAces = reduced;
    }
}