package ru.nsu.oop.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.oop.cards.Card;
import ru.nsu.oop.cards.Rank;
import ru.nsu.oop.cards.Suit;
import ru.nsu.oop.player.Dealer;
import ru.nsu.oop.player.Player;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoundTest {

    private TestDeck deck;
    private StubView view;
    private Player player;
    private Dealer dealer;
    private Round round;

    @BeforeEach
    void setUp() {
        deck = new TestDeck();
        view = new StubView();
        player = new Player();
        dealer = new Dealer();
        round = new Round(deck, player, dealer, view, 1);
    }

    // ---------- Вспомогательные конструкторы карт ----------

    private static Card card(Suit suit, Rank rank) {
        return new Card(suit, rank);
    }

    private static Card ten() {
        return card(Suit.SPADES, Rank.TEN);
    }

    // ---------- Общие сценарии ----------

    @Test
    void playAnnouncesRoundStart() {
        deck.addCards(ten(), ten(), ten(), ten());
        view.addPlayerAction(false);

        round.play();

        assertTrue(view.getCalls().contains("showRoundStart 1"));
    }

    @Test
    void playDealsTwoCardsToEach() {
        deck.addCards(ten(), ten(), ten(), ten());
        view.addPlayerAction(false);

        round.play();

        assertEquals(2, player.getHand().size());
        assertEquals(2, dealer.getHand().size());
    }

    @Test
    void playShowsDealerHandHiddenFirst() {
        deck.addCards(ten(), ten(), ten(), ten());
        view.addPlayerAction(false);

        round.play();

        assertTrue(view.getCalls().contains("showDealerHand true"));
    }

    // ---------- Определение победителя ----------

    @Test
    void playerWinsWithHigherSum() {
        // P: 10+10=20, D: 10+7=17 — дилер не добирает
        deck.addCards(
                card(Suit.SPADES, Rank.TEN),
                card(Suit.HEARTS, Rank.TEN),
                card(Suit.DIAMONDS, Rank.TEN),
                card(Suit.CLUBS, Rank.SEVEN)
        );
        view.addPlayerAction(false);

        RoundResult result = round.play();

        assertEquals(RoundResult.PLAYER_WIN, result);
    }

    @Test
    void dealerWinsWithHigherSum() {
        // P: 10+7=17, D: 10+9=19
        deck.addCards(
                card(Suit.SPADES, Rank.TEN),
                card(Suit.HEARTS, Rank.TEN),
                card(Suit.DIAMONDS, Rank.SEVEN),
                card(Suit.CLUBS, Rank.NINE)
        );
        view.addPlayerAction(false);

        RoundResult result = round.play();

        assertEquals(RoundResult.DEALER_WIN, result);
    }

    @Test
    void drawWithEqualSums() {
        // P: 10+7=17, D: 10+7=17
        deck.addCards(
                card(Suit.SPADES, Rank.TEN),
                card(Suit.HEARTS, Rank.TEN),
                card(Suit.DIAMONDS, Rank.SEVEN),
                card(Suit.CLUBS, Rank.SEVEN)
        );
        view.addPlayerAction(false);

        RoundResult result = round.play();

        assertEquals(RoundResult.DRAW, result);
    }

    @Test
    void playerBustLeadsToDealerWin() {
        // P: 10+10, потом тянет 10 → 30 (перебор)
        deck.addCards(ten(), ten(), ten(), ten(), ten());
        view.addPlayerAction(true);

        RoundResult result = round.play();

        assertEquals(RoundResult.DEALER_WIN, result);
    }

    @Test
    void dealerBustLeadsToPlayerWin() {
        // P: 10+7=17. D: 10+6=16, тянет 10 → 26 (перебор)
        deck.addCards(
                card(Suit.SPADES, Rank.TEN),
                card(Suit.HEARTS, Rank.TEN),
                card(Suit.DIAMONDS, Rank.SEVEN),
                card(Suit.CLUBS, Rank.SIX),
                card(Suit.SPADES, Rank.TEN)
        );
        view.addPlayerAction(false);

        RoundResult result = round.play();

        assertEquals(RoundResult.PLAYER_WIN, result);
    }

    // ---------- Ход игрока ----------

    @Test
    void playerTurnDrawsCardWhenAsked() {
        deck.addCards(ten(), ten(), ten(), ten(), card(Suit.SPADES, Rank.TWO));
        view.addPlayerAction(true);   // взять карту
        view.addPlayerAction(false);  // остановиться

        round.play();

        assertEquals(3, player.getHand().size());
        assertTrue(view.getCalls().contains("showPlayerDrawsCard"));
    }

    @Test
    void playerTurnStopsOnBlackjack() {
        // P: A + K = 21 (блэкджек). Player turn не должен запрашивать добор.
        deck.addCards(
                card(Suit.SPADES, Rank.ACE),
                card(Suit.HEARTS, Rank.TEN),
                card(Suit.DIAMONDS, Rank.KING),
                card(Suit.CLUBS, Rank.SEVEN)
        );
        // Явно оставляем только один ответ на случай, если код всё-таки спросит
        view.addPlayerAction(false);

        round.play();

        long askCount = view.getCalls().stream()
                .filter(c -> c.equals("askPlayerAction"))
                .count();
        assertEquals(0, askCount, "при блэкджеке игрока спрашивать нельзя");
    }

    // ---------- Ход дилера ----------

    @Test
    void dealerDrawsWhileBelow17() {
        // P: 10+7=17. D: 10+6=16 → тянет 5 → 21 → стоп.
        deck.addCards(
                card(Suit.SPADES, Rank.TEN),
                card(Suit.HEARTS, Rank.TEN),
                card(Suit.DIAMONDS, Rank.SEVEN),
                card(Suit.CLUBS, Rank.SIX),
                card(Suit.SPADES, Rank.FIVE)
        );
        view.addPlayerAction(false);

        round.play();

        assertEquals(3, dealer.getHand().size());
        assertTrue(view.getCalls().contains("showDealerDrawsCard"));
    }

    @Test
    void dealerStopsAt17() {
        // P: 10+7. D: 10+7=17 → не должен добирать.
        deck.addCards(
                card(Suit.SPADES, Rank.TEN),
                card(Suit.HEARTS, Rank.TEN),
                card(Suit.DIAMONDS, Rank.SEVEN),
                card(Suit.CLUBS, Rank.SEVEN)
        );
        view.addPlayerAction(false);

        round.play();

        assertEquals(2, dealer.getHand().size());
        assertTrue(view.getCalls().stream()
                .noneMatch(c -> c.equals("showDealerDrawsCard")));
    }

    @Test
    void dealerOpensHiddenCardBeforeDrawing() {
        deck.addCards(ten(), ten(), ten(), ten());
        view.addPlayerAction(false);

        round.play();

        int openIndex = view.getCalls().indexOf("showDealerOpensHiddenCard");
        int turnIndex = view.getCalls().indexOf("showDealerTurn");
        assertTrue(turnIndex >= 0 && openIndex > turnIndex,
                "дилер должен сначала начать ход, потом открыть закрытую карту");
    }

    // ---------- Прямой тест determineWinner ----------

    @Test
    void determineWinnerHandlesAllCases() {
        // Игрок перебрал
        player.addCard(card(Suit.SPADES, Rank.TEN));
        player.addCard(card(Suit.HEARTS, Rank.TEN));
        player.addCard(card(Suit.DIAMONDS, Rank.TWO));
        dealer.addCard(card(Suit.SPADES, Rank.TEN));
        dealer.addCard(card(Suit.HEARTS, Rank.SEVEN));
        assertEquals(RoundResult.DEALER_WIN, round.determineWinner());

        // Сброс и обратный случай
        player.resetHand();
        dealer.resetHand();
        player.addCard(card(Suit.SPADES, Rank.TEN));
        player.addCard(card(Suit.HEARTS, Rank.SEVEN));
        dealer.addCard(card(Suit.SPADES, Rank.KING));
        dealer.addCard(card(Suit.HEARTS, Rank.KING));
        dealer.addCard(card(Suit.DIAMONDS, Rank.TWO));
        assertEquals(RoundResult.PLAYER_WIN, round.determineWinner());

        // Ничья
        player.resetHand();
        dealer.resetHand();
        player.addCard(card(Suit.SPADES, Rank.TEN));
        player.addCard(card(Suit.HEARTS, Rank.SEVEN));
        dealer.addCard(card(Suit.SPADES, Rank.TEN));
        dealer.addCard(card(Suit.HEARTS, Rank.SEVEN));
        assertEquals(RoundResult.DRAW, round.determineWinner());
    }
}