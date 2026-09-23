package ru.nsu.oop.view;

import ru.nsu.oop.cards.Card;
import ru.nsu.oop.cards.Hand;
import ru.nsu.oop.game.RoundResult;

/**
 * Контракт представления: описывает, что игра умеет сообщать пользователю
 * и что она у него спрашивает.
 * Не содержит ни одного текста — только события.
 */
public interface View {

    /**
     * Показывает приветствие при старте игры.
     */
    void showWelcome();

    /**
     * Сообщает о начале нового раунда.
     *
     * @param roundNumber номер раунда
     */
    void showRoundStart(int roundNumber);

    /**
     * Показывает карты игрока.
     *
     * @param hand рука игрока
     */
    void showPlayerHand(Hand hand);

    /**
     * Показывает карты дилера.
     *
     * @param hand     рука дилера
     * @param hideCard скрывать ли закрытую карту
     */
    void showDealerHand(Hand hand, boolean hideCard);

    /**
     * Сообщает о начале хода игрока.
     */
    void showPlayerTurn();

    /**
     * Сообщает о начале хода дилера.
     */
    void showDealerTurn();

    /**
     * Сообщает, что игрок взял карту.
     *
     * @param card         взятая карта
     * @param displayValue значение карты для отображения
     */
    void showPlayerDrawsCard(Card card, int displayValue);

    /**
     * Сообщает, что дилер взял карту.
     *
     * @param card         взятая карта
     * @param displayValue значение карты для отображения
     */
    void showDealerDrawsCard(Card card, int displayValue);

    /**
     * Сообщает, что дилер открыл закрытую карту.
     *
     * @param card         открытая карта
     * @param displayValue значение карты для отображения
     */
    void showDealerOpensHiddenCard(Card card, int displayValue);

    /**
     * Спрашивает у игрока, хочет ли он взять карту.
     *
     * @return {@code true}, если игрок берёт карту; {@code false} — если останавливается
     */
    boolean askPlayerAction();

    /**
     * Показывает результат раунда и текущий счёт.
     *
     * @param result     результат раунда
     * @param playerWins количество побед игрока
     * @param dealerWins количество побед дилера
     */
    void showResult(RoundResult result, int playerWins, int dealerWins);
}