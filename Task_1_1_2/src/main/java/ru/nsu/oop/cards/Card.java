package ru.nsu.oop.cards;

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

    /**
     * Возвращает строковое представление карты с учетом грамматического рода её ранга
     * и указанного отображаемого числового значения.
     * <p>
     * Метод автоматически согласует масть и ранг (например, "Пиковая дама",
     * "Пиковый король" или "Туз Пики") и добавляет числовое значение в скобках.
     * </p>
     *
     * @param displayValue числовое значение карты для отображения (текущие очки в игре)
     * @return строковое описание карты в формате "Название (Значение)"
     */
    public String toString(int displayValue) {
        String name = switch (rank) {
            case QUEEN -> suit.getFeminine() + " " + rank.getDisplayName().toLowerCase();
            case KING -> suit.getMasculine() + " " + rank.getDisplayName().toLowerCase();
            default -> rank.getDisplayName() + " " + suit.getBaseName();
        };
        return name + " (" + displayValue + ")";
    }
}