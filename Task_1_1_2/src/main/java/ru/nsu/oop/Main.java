package ru.nsu.oop;

/**
 * Точка входа приложения: собирает игру и запускает её.
 */
public class Main {

    /**
     * Создаёт игру с консольным представлением и запускает её.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        View view = new ConsoleView();
        Game game = new Game(view);
        game.start();
    }
}