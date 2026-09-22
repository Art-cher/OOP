package ru.nsu.oop;

public class Dealer extends Participant {
    private static final int HIDDEN_CARD = 1;

    public int getHiddenCardIndex(){
        return HIDDEN_CARD;
    }
    /**
     * Возвращает true, если дилер обязан взять ещё одну карту.
     * Правило: брать, пока сумма меньше 17.
     */
    public boolean shouldDraw() {
        return hand.getSum() < 17;
    }
}