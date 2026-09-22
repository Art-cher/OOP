package ru.nsu.oop;


/**
 * Управляет игровым процессом: раздачей, ходами, определением победителя
 * и ведением счёта. Работает только с моделью и через интерфейс {@link View} —
 * не знает ни одного текста и ни одной детали консоли.
 */
public class Game {

    private final Deck deck;
    private final Player player;
    private final Dealer dealer;
    private final View view;
    private int roundNumber;

    /**
     * @param view представление, через которое игра общается с пользователем
     */
    public Game(View view) {
        this.view = view;
        this.deck = new Deck();
        this.player = new Player();
        this.dealer = new Dealer();
        this.roundNumber = 0;
    }

    /**
     * Запускает бесконечный цикл раундов. Каждый раунд начинается с чистых рук.
     */
    public void start() {
        view.showWelcome();
        while (true) {
            playRound();
        }
    }

    /**
     * Проводит один раунд: раздача, ход игрока, ход дилера, определение победителя.
     */
    private void playRound() {
        roundNumber++;
        view.showRoundStart(roundNumber);

        player.resetHand();
        dealer.resetHand();

        dealInitialCards();
        view.showPlayerHand(player.getHand());
        view.showDealerHand(dealer.getHand(), true);

        playerTurn();

        dealerTurn();

        RoundResult result = determineWinner();
        applyResult(result);
        view.showResult(result, player.getWins(), dealer.getWins());
    }

    /**
     * Раздаёт по две карты игроку и дилеру. Вторая карта дилера остаётся закрытой.
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
        while (view.askPlayerAction()) {
            Card card = deck.drawCard();
            player.addCard(card);
            int displayValue = player.getHand().getDisplayValue(player.getHand().size() - 1);
            view.showPlayerDrawsCard(card, displayValue);
            view.showPlayerHand(player.getHand());
            view.showDealerHand(dealer.getHand(), true);
            if (player.isBlackjack() || player.isBlackjack()) {
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
     */
    private RoundResult determineWinner() {
        // Перебор важнее суммы
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

    /**
     * Начисляет победу тому, кто выиграл раунд. При ничьей счёт не меняется.
     */
    private void applyResult(RoundResult result) {
        switch (result) {
            case PLAYER_WIN -> player.incrementWins();
            case DEALER_WIN -> dealer.incrementWins();
        }
    }
}