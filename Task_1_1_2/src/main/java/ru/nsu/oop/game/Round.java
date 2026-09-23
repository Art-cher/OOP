package ru.nsu.oop.game;

import ru.nsu.oop.player.Dealer;
import ru.nsu.oop.player.Player;
import ru.nsu.oop.view.View;
import ru.nsu.oop.cards.Card;
import ru.nsu.oop.cards.Deck;

/**
 * Один раунд блэкджека: раздача, ходы, определение победителя.
 */
public class Round {
    private final Deck deck;
    private final Player player;
    private final Dealer dealer;
    private final View view;
    private final int number;

    public Round(Deck deck, Player player, Dealer dealer, View view, int number) {
        this.deck = deck;
        this.player = player;
        this.dealer = dealer;
        this.view = view;
        this.number = number;
    }

    /**
     * Проводит раунд от раздачи до показа результата.
     *
     * @return результат раунда
     */
    public RoundResult play() {
        view.showRoundStart(number);

        player.resetHand();
        dealer.resetHand();

        dealInitialCards();
        view.showPlayerHand(player.getHand());
        view.showDealerHand(dealer.getHand(), true);

        playerTurn();
        dealerTurn();

        return determineWinner();
    }

    /**
     * Раздаёт по две карты игроку и дилеру.
     * Вторая карта дилера остаётся закрытой.
     */
    private void dealInitialCards() {
        player.addCard(deck.drawCard());
        dealer.addCard(deck.drawCard());
        player.addCard(deck.drawCard());
        dealer.addCard(deck.drawCard());
    }

    /**
     * Ход игрока: спрашивает действие, пока игрок не остановится или не переберёт.
     */
    private void playerTurn() {
        view.showPlayerTurn();
        if (player.isEnd()) {
            return;
        }
        while (view.askPlayerAction()) {
            Card card = deck.drawCard();
            player.addCard(card);
            int displayValue = player.getHand().getDisplayValue(player.getHand().size() - 1);
            view.showPlayerDrawsCard(card, displayValue);
            view.showPlayerHand(player.getHand());
            view.showDealerHand(dealer.getHand(), true);
            if (player.isEnd()) {
                return;
            }
        }
    }

    /**
     * Ход дилера: открывает закрытую карту и добирает, пока сумма меньше 17.
     */
    private void dealerTurn() {
        view.showDealerTurn();
        int hiddenIndex = dealer.getHiddenCardIndex();
        Card opened = dealer.getHand().getCard(hiddenIndex);
        int displayValue = dealer.getHand().getDisplayValue(hiddenIndex);
        view.showDealerOpensHiddenCard(opened, displayValue);
        view.showPlayerHand(player.getHand());
        view.showDealerHand(dealer.getHand(), false);

        while (dealer.shouldDraw()) {
            Card card = deck.drawCard();
            dealer.addCard(card);
            displayValue = dealer.getHand().getDisplayValue(dealer.getHand().size() - 1);
            view.showDealerDrawsCard(card, displayValue);
            view.showPlayerHand(player.getHand());
            view.showDealerHand(dealer.getHand(), false);
        }
    }

    /**
     * Сравнивает суммы рук и определяет исход раунда.
     *
     * @return результат раунда
     */
    RoundResult determineWinner() {
        if (player.isBust()) {
            return RoundResult.DEALER_WIN;
        }
        if (dealer.isBust()) {
            return RoundResult.PLAYER_WIN;
        }

        int playerSum = player.getHand().getSum();
        int dealerSum = dealer.getHand().getSum();
        if (playerSum > dealerSum) {
            return RoundResult.PLAYER_WIN;
        }
        if (dealerSum > playerSum) {
            return RoundResult.DEALER_WIN;
        }
        return RoundResult.DRAW;
    }
}
