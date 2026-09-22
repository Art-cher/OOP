package ru.nsu.oop;

/**
 * Контракт представления: описывает, что игра умеет сообщать пользователю
 * и что она у него спрашивает. Не содержит ни одного текста — только события.
 */
public interface View {
    void showWelcome();
    void showRoundStart(int roundNumber);
    void showPlayerHand(Hand hand);
    void showDealerHand(Hand hand, boolean hideCard);

    void showPlayerTurn();
    void showDealerTurn();
    void showPlayerDrawsCard(Card card, int displayValue);
    void showDealerDrawsCard(Card card, int displayValue);
    void showDealerOpensHiddenCard(Card card, int displayValue);

    boolean askPlayerAction();
    void showResult(RoundResult result, int playerWins, int dealerWins);
}