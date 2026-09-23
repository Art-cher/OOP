package ru.nsu.oop.cards;

/**
 * Перечисление, представляющее карточные масти с поддержкой различных грамматических форм на русском языке.
 */
public enum Suit {
    SPADES("Пики", "Пиковая", "Пиковый"),
    HEARTS("Червы", "Червовая", "Червовый"),
    DIAMONDS("Бубны", "Бубновая", "Бубновый"),
    CLUBS("Трефы", "Трефовая", "Трефовый");

    private final String baseName;
    private final String feminine;
    private final String masculine;

    Suit(String baseName, String feminine, String masculine) {
        this.baseName = baseName;
        this.feminine = feminine;
        this.masculine = masculine;
    }

    /**
     * Возвращает базовое название масти во множественном числе.
     *
     * @return строка с базовым названием масти
     */
    public String getBaseName() {
        return baseName;
    }

    /**
     * Возвращает название масти в форме женского рода.
     *
     * @return строка с формой женского рода
     */
    public String getFeminine() {
        return feminine;
    }

    /**
     * Возвращает название масти в форме мужского рода.
     *
     * @return строка с формой мужского рода
     */
    public String getMasculine() {
        return masculine;
    }
}