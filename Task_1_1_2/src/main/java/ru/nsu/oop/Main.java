package ru.nsu.oop;

import ru.nsu.oop.game.Game;
import ru.nsu.oop.view.ConsoleView;
import ru.nsu.oop.view.View;

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