package ru.nsu.oop;

/**
 * Игральная карта — пара «масть + достоинство».
 *
 * @param suit масть карты
 * @param rank достоинство карты
 */
public record Card(Suit suit, Rank rank) {

    /**
     * Возвращает номинальное значение карты по достоинству.
     *
     * @return значение карты
     */
    public int getValue() {
        return rank.getValue();
    }

    /**
     * Проверяет, является ли карта тузом.
     *
     * @return {@code true}, если достоинство карты — туз
     */
    public boolean isAce() {
        return rank.isAce();
    }
}