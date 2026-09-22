package ru.nsu.oop;

public record Card(Suit suit, Rank rank) {

    public int getValue() {
        return rank.getValue();
    }

    public boolean isAce() {
        return rank.isAce();
    }
}