package ru.nsu.oop.game;

import ru.nsu.oop.cards.Deck;
import ru.nsu.oop.player.Dealer;
import ru.nsu.oop.player.Player;
import ru.nsu.oop.view.View;

/**
 * Управляет игровым процессом: раздачей, ходами, определением победителя
 * и ведением счёта.
 * Работает только с моделью и через интерфейс {@link View} — не знает ни одного
 * текста и ни одной детали консоли.
 */
public class Game {

    private final Deck deck;
    private final Player player;
    private final Dealer dealer;
    private final View view;
    private int roundNumber;

    /**
     * Создаёт игру.
     *
     * @param view представление, через которое игра общается с пользователем
     */
    public Game(View view) {
        this(view, new Deck());
    }

    /**
     * метод для тестов с возможностью передать свою доску
     * @param view тестовое представление, через которое игра "общается с пользователем"
     * @param deck тестовая доска
     */
    Game(View view, Deck deck) {
        this.view = view;
        this.deck = deck;
        this.player = new Player();
        this.dealer = new Dealer();
        this.roundNumber = 0;
    }

    /**
     * Запускает бесконечный цикл раундов.
     * Каждый раунд начинается с чистых рук.
     */
    public void start() {
        view.showWelcome();
        while (true) {
            roundNumber++;
            Round round = new Round(deck, player, dealer, view, roundNumber);
            RoundResult result = round.play();
            applyResult(result);
            view.showResult(result, player.getWins(), dealer.getWins());
        }
    }

    /**
     * Начисляет победу тому, кто выиграл раунд.
     * При ничьей счёт не меняется.
     *
     * @param result результат раунда
     */
    void applyResult(RoundResult result) {
        switch (result) {
            case PLAYER_WIN -> player.incrementWins();
            case DEALER_WIN -> dealer.incrementWins();
            default -> {
                // DRAW — счёт не меняется
            }
        }
    }

    Player getPlayer() {
        return player;
    }

    Dealer getDealer() {
        return dealer;
    }
}