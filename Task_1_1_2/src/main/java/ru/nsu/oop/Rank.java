package ru.nsu.oop;

/**
 * Достоинство игральной карты и связанное с ним номинальное значение.
 */
public enum Rank {
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(10),
    QUEEN(10),
    KING(10),
    ACE(11);

    private final int value;

    Rank(int value) {
        this.value = value;
    }

    /**
     * Возвращает номинальное значение достоинства.
     * Туз возвращает 11, картинки — 10.
     *
     * @return значение карты по достоинству
     */
    public int getValue() {
        return value;
    }

    /**
     * Проверяет, является ли достоинство тузом.
     *
     * @return {@code true}, если это туз
     */
    public boolean isAce() {
        return this == ACE;
    }
}