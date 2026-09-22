package ru.nsu.oop;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

class StubView implements View {
    private final Queue<Boolean> playerActions = new ArrayDeque<>();
    private final List<String> calls = new ArrayList<>();
    private boolean throwOnShowResult = false;

    public void addPlayerAction(boolean action) {
        playerActions.add(action);
    }

    public void setThrowOnShowResult(boolean throwOnShowResult) {
        this.throwOnShowResult = throwOnShowResult;
    }

    public List<String> getCalls() {
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
        calls.add("showPlayerDrawsCard " + card + " " + displayValue);
    }

    @Override
    public void showDealerDrawsCard(Card card, int displayValue) {
        calls.add("showDealerDrawsCard " + card + " " + displayValue);
    }

    @Override
    public void showDealerOpensHiddenCard(Card card, int displayValue) {
        calls.add("showDealerOpensHiddenCard " + card + " " + displayValue);
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
        calls.add("showResult " + result + " " + playerWins + " " + dealerWins);
        if (throwOnShowResult) {
            throw new RuntimeException("Stop game loop");
        }
    }
}