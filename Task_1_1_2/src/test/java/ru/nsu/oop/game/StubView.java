package ru.nsu.oop.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import ru.nsu.oop.cards.Card;
import ru.nsu.oop.cards.Hand;
import ru.nsu.oop.view.View;

/**
 * Заглушка представления для тестов.
 * Ничего не печатает, а записывает события в журнал и отдаёт заранее
 * заготовленные ответы игрока.
 */
class StubView implements View {

    private final Queue<Boolean> playerActions = new ArrayDeque<>();
    private final List<String> calls = new ArrayList<>();

    /** Сколько ещё вызовов showResult пропустить перед тем, как упасть. -1 — не падать. */
    private int resultsToSkip = -1;

    void addPlayerAction(boolean action) {
        playerActions.add(action);
    }

    /** Падать на первом же вызове showResult. */
    void setThrowOnShowResult(boolean value) {
        this.resultsToSkip = value ? 0 : -1;
    }

    /**
     * Падать на (count + 1)-м вызове showResult.
     * Например, {@code setThrowAfterResults(1)} пропустит первый результат
     * и упадёт на втором — это позволяет проиграть два раунда.
     */
    void setThrowAfterResults(int count) {
        this.resultsToSkip = count;
    }

    List<String> getCalls() {
        return calls;
    }

    @Override
    public void showWelcome() {
        calls.add("showWelcome");
    }

    @Override
    public void showRoundStart(int roundNumber) {
        calls.add("showRoundStart " + roundNumber);
    }

    @Override
    public void showPlayerHand(Hand hand) {
        calls.add("showPlayerHand");
    }

    @Override
    public void showDealerHand(Hand hand, boolean hideCard) {
        calls.add("showDealerHand " + hideCard);
    }

    @Override
    public void showPlayerTurn() {
        calls.add("showPlayerTurn");
    }

    @Override
    public void showDealerTurn() {
        calls.add("showDealerTurn");
    }

    @Override
    public void showPlayerDrawsCard(Card card, int displayValue) {
        calls.add("showPlayerDrawsCard");
    }

    @Override
    public void showDealerDrawsCard(Card card, int displayValue) {
        calls.add("showDealerDrawsCard");
    }

    @Override
    public void showDealerOpensHiddenCard(Card card, int displayValue) {
        calls.add("showDealerOpensHiddenCard");
    }

    @Override
    public boolean askPlayerAction() {
        calls.add("askPlayerAction");
        if (playerActions.isEmpty()) {
            return false;
        }
        return playerActions.poll();
    }

    @Override
    public void showResult(RoundResult result, int playerWins, int dealerWins) {
        calls.add("showResult " + result + " " + playerWins + ":" + dealerWins);
        if (resultsToSkip >= 0) {
            if (resultsToSkip == 0) {
                throw new RuntimeException("stop game loop");
            }
            resultsToSkip--;
        }
    }
}