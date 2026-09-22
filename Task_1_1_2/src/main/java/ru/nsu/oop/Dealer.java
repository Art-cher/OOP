package ru.nsu.oop;

/**
 * Дилер в блэкджеке.
 * Знает, какая из его карт закрыта, и по какому правилу добирает карты.
 */
public class Dealer extends Participant {
    private static final int HIDDEN_CARD = 1;

    /**
     * Возвращает индекс закрытой карты дилера.
     *
     * @return индекс закрытой карты
     */
    public int getHiddenCardIndex() {
        return HIDDEN_CARD;
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