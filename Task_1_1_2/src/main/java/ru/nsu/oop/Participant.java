package ru.nsu.oop;

public abstract class Participant {
    protected final Hand hand;
    protected int wins;

    public Participant() {
        this.hand = new Hand();
        this.wins = 0;
    }

    public void addCard(Card card) { hand.addCard(card); }
    public Hand getHand() { return hand; }
    public int getWins() { return wins; }
    public void incrementWins() { wins++; }
    public void resetHand() { hand.clear(); }
    public boolean isBlackjack() { return hand.isBlackjack(); }
    public boolean isBust() { return hand.isBust(); }
}