package ru.nsu.oop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Колода карт из нескольких стандартных наборов.
 * Поддерживает перемешивание и выдачу карт с автоматическим восстановлением,
 * когда карты заканчиваются.
 */
public class Deck {
    private static final int NUMBER_OF_DECKS = 8;

    private final List<Card> cards;
    private final Random random;

    /**
     * Создаёт колоду из {@value #NUMBER_OF_DECKS} стандартных наборов
     * и перемешивает её.
     */
    public Deck() {
        this.cards = new ArrayList<>();
        this.random = new Random();
        fillAndShuffle();
    }

    /**
     * Заполняет колоду всеми картами и перемешивает её.
     */
    private void fillAndShuffle() {
        for (int i = 0; i < NUMBER_OF_DECKS; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(suit, rank));
                }
            }
        }

        Collections.shuffle(cards, random);
    }

    /**
     * Выдаёт верхнюю карту из колоды.
     * Если карты закончились, колода автоматически заполняется и перемешивается заново.
     *
     * @return выданная карта
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            reset();
        }
        return cards.removeLast();
    }

    /**
     * Очищает колоду и заполняет её заново.
     */
    private void reset() {
        cards.clear();
        fillAndShuffle();
    }
}