package ru.nsu.oop.player;

/**
 * Дилер в блэкджеке.
 * Знает, какая из его карт закрыта, и по какому правилу добирает карты.
 */
public class Dealer extends Participant {
    private static final int HIDDEN_CARD_INDEX = 1;

    /**
     * Возвращает индекс закрытой карты дилера.
     *
     * @return индекс закрытой карты
     */
    public int getHiddenCardIndex() {
        return HIDDEN_CARD_INDEX;
    }

    /**
     * Возвращает {@code true}, если дилер обязан взять ещё одну карту.
     * Правило: брать, пока сумма меньше 17.
     *
     * @return {@code true}, если дилер должен добрать карту
     */
    public boolean shouldDraw() {
        return hand.getSum() < 17;
    }
}