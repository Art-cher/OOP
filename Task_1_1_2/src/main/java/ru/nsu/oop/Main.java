package ru.nsu.oop;

public class Main {
    public static void main(String[] args) {
        View view = new ConsoleView();
        Game game = new Game(view);
        game.start();
    }
}