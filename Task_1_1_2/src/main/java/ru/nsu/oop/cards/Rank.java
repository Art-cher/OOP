package ru.nsu.oop.cards;

/**
 * Достоинство игральной карты и связанное с ним номинальное значение.
 */
public enum Rank {
    TWO(2, "Двойка"),
    THREE(3, "Тройка"),
    FOUR(4, "Четвёрка"),
    FIVE(5, "Пятёрка"),
    SIX(6, "Шестёрка"),
    SEVEN(7, "Семёрка"),
    EIGHT(8, "Восьмёрка"),
    NINE(9, "Девятка"),
    TEN(10, "Десятка"),
    JACK(10, "Валет"),
    QUEEN(10, "Дама"),
    KING(10, "Король"),
    ACE(11, "Туз");

    private final int value;
    private final String displayName;

    Rank(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
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
     * Возвращает русское имя достоинства.
     *
     * @return имя карты по достоинству
     */
    public String getDisplayName() {
        return displayName;
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