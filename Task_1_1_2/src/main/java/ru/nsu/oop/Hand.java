package ru.nsu.oop;

import java.util.ArrayList;
import java.util.List;

/**
 * Рука участника: набор карт с подсчётом суммы и обработкой тузов.
 * Туз считается как 11 до тех пор, пока это не приводит к перебору;
 * иначе он понижается до 1.
 */
public class Hand {
    private final List<Card> cards;

    /**
     * Создаёт пустую руку.
     */
    public Hand() {
        this.cards = new ArrayList<>();
    }

    /**
     * Добавляет карту в руку.
     *
     * @param card карта для добавления
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Возвращает сумму карт в руке с учётом понижения тузов.
     *
     * @return сумма карт
     */
    public int getSum() {
        int baseSum = 0;
        for (Card c : cards) {
            baseSum += c.getValue();
        }
        return baseSum - countReducedAces() * 10;
    }

    /**
     * Проверяет, собрана ли в руке комбинация «блэкджек».
     * Блэкджек — это ровно две карты, дающие в сумме 21.
     *
     * @return {@code true}, если у руки блэкджек
     */
    public boolean isBlackjack() {
        return cards.size() == 2 && getSum() == 21;
    }

    /**
     * Проверяет, перебрала ли рука (сумма карт больше 21).
     *
     * @return {@code true}, если сумма карт больше 21
     */
    public boolean isBust() {
        return getSum() > 21;
    }

    /**
     * Очищает руку.
     */
    public void clear() {
        cards.clear();
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

        int reducedAces = countReducedAces();

        int aceOrder = 0;
        for (int i = 0; i < index; i++) {
            if (cards.get(i).isAce()) {
                aceOrder++;
            }
        }

        return aceOrder < reducedAces ? 1 : 11;
    }

    /**
     * Считает, сколько тузов понижено, чтобы сумма помещалась в 21.
     *
     * @return количество пониженных тузов
     */
    private int countReducedAces() {
        int baseSum = 0;
        int aces = 0;
        for (Card c : cards) {
            baseSum += c.getValue();
            if (c.isAce()) {
                aces++;
            }
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
     * @param index позиция карты, от 0 до {@code size() - 1}
     * @return карта на указанной позиции
     */
    public Card getCard(int index) {
        return cards.get(index);
    }
}