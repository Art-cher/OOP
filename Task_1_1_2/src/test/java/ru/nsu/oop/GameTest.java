package ru.nsu.oop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private StubView view;
    private TestDeck testDeck;

    @BeforeEach
    void setUp() throws Exception {
        view = new StubView();
        game = new Game(view);
        testDeck = new TestDeck();
        Field deckField = Game.class.getDeclaredField("deck");
        deckField.setAccessible(true);
        deckField.set(game, testDeck);
    }

    @Test
    void testStartRunsOneRoundAndStops() {
        // 4 карты по 10: игрок 20, дилер 20 — дилер не добирает, колода не истощается
        for (int i = 0; i < 4; i++) {
            testDeck.addCard(new Card(Suit.SPADES, Rank.TEN));
        }
        view.addPlayerAction(false); // игрок останавливается
        view.setThrowOnShowResult(true);

        assertThrows(RuntimeException.class, () -> game.start());

        assertTrue(view.getCalls().contains("showWelcome"));
        assertTrue(view.getCalls().contains("showRoundStart 1"));
        assertTrue(view.getCalls().contains("showPlayerHand"));
        assertTrue(view.getCalls().contains("showDealerHand true"));
        assertTrue(view.getCalls().contains("showPlayerTurn"));
        assertTrue(view.getCalls().contains("showDealerTurn"));

        // У этих событий в StubView есть аргументы в логе — сравниваем по префиксу
        assertTrue(view.getCalls().stream()
                .anyMatch(c -> c.startsWith("showDealerOpensHiddenCard")));
        assertTrue(view.getCalls().stream()
                .anyMatch(c -> c.startsWith("showResult")));
    }

    @Test
    void testPlayerTurnStand() throws Exception {
        Player player = getPlayer();
        player.addCard(new Card(Suit.SPADES, Rank.TEN));
        player.addCard(new Card(Suit.HEARTS, Rank.SEVEN));
        view.addPlayerAction(false);
        Method playerTurn = Game.class.getDeclaredMethod("playerTurn");
        playerTurn.setAccessible(true);
        playerTurn.invoke(game);
        assertEquals(2, player.getHand().size());
    }

    @Test
    void testPlayerTurnDrawsCard() throws Exception {
        Player player = getPlayer();
        player.addCard(new Card(Suit.SPADES, Rank.TEN));
        player.addCard(new Card(Suit.HEARTS, Rank.SEVEN));
        view.addPlayerAction(true);
        view.addPlayerAction(false);
        testDeck.addCard(new Card(Suit.DIAMONDS, Rank.TWO));
        Method playerTurn = Game.class.getDeclaredMethod("playerTurn");
        playerTurn.setAccessible(true);
        playerTurn.invoke(game);
        assertEquals(3, player.getHand().size());
        assertTrue(view.getCalls().stream().anyMatch(c -> c.startsWith("showPlayerDrawsCard")));
    }

    @Test
    void testPlayerTurnBustStops() throws Exception {
        Player player = getPlayer();
        player.addCard(new Card(Suit.SPADES, Rank.TEN));
        player.addCard(new Card(Suit.HEARTS, Rank.SIX));
        player.addCard(new Card(Suit.DIAMONDS, Rank.FIVE)); // сумма 21
        view.addPlayerAction(true);
        testDeck.addCard(new Card(Suit.CLUBS, Rank.TWO)); // сумма 23, перебор
        Method playerTurn = Game.class.getDeclaredMethod("playerTurn");
        playerTurn.setAccessible(true);
        playerTurn.invoke(game);
        assertEquals(4, player.getHand().size());
        long askCount = view.getCalls().stream().filter(c -> c.equals("askPlayerAction")).count();
        assertEquals(1, askCount);
    }

    @Test
    void testDealerTurn() throws Exception {
        Dealer dealer = getDealer();
        dealer.addCard(new Card(Suit.SPADES, Rank.TEN));
        dealer.addCard(new Card(Suit.HEARTS, Rank.SIX)); // сумма 16, должен брать
        testDeck.addCard(new Card(Suit.DIAMONDS, Rank.FIVE)); // сумма 21
        Method dealerTurn = Game.class.getDeclaredMethod("dealerTurn");
        dealerTurn.setAccessible(true);
        dealerTurn.invoke(game);
        assertEquals(3, dealer.getHand().size());
        assertTrue(view.getCalls().stream().anyMatch(c -> c.startsWith("showDealerOpensHiddenCard")));
        assertTrue(view.getCalls().stream().anyMatch(c -> c.startsWith("showDealerDrawsCard")));
    }

    @Test
    void testDetermineWinner() throws Exception {
        Method determineWinner = Game.class.getDeclaredMethod("determineWinner");
        determineWinner.setAccessible(true);
        Player player = getPlayer();
        Dealer dealer = getDealer();

        // Игрок перебрал
        player.addCard(new Card(Suit.SPADES, Rank.KING));
        player.addCard(new Card(Suit.HEARTS, Rank.KING));
        player.addCard(new Card(Suit.DIAMONDS, Rank.TWO)); // сумма 22
        dealer.addCard(new Card(Suit.SPADES, Rank.TEN));
        dealer.addCard(new Card(Suit.HEARTS, Rank.SEVEN)); // сумма 17
        RoundResult result = (RoundResult) determineWinner.invoke(game);
        assertEquals(RoundResult.DEALER_WIN, result);

        // Дилер перебрал
        player.resetHand();
        dealer.resetHand();
        player.addCard(new Card(Suit.SPADES, Rank.TEN));
        player.addCard(new Card(Suit.HEARTS, Rank.SEVEN)); // сумма 17
        dealer.addCard(new Card(Suit.SPADES, Rank.KING));
        dealer.addCard(new Card(Suit.HEARTS, Rank.KING));
        dealer.addCard(new Card(Suit.DIAMONDS, Rank.TWO)); // сумма 22
        result = (RoundResult) determineWinner.invoke(game);
        assertEquals(RoundResult.PLAYER_WIN, result);

        // У игрока больше
        player.resetHand();
        dealer.resetHand();
        player.addCard(new Card(Suit.SPADES, Rank.KING));
        player.addCard(new Card(Suit.HEARTS, Rank.NINE)); // сумма 19
        dealer.addCard(new Card(Suit.SPADES, Rank.TEN));
        dealer.addCard(new Card(Suit.HEARTS, Rank.SEVEN)); // сумма 17
        result = (RoundResult) determineWinner.invoke(game);
        assertEquals(RoundResult.PLAYER_WIN, result);

        // У дилера больше
        player.resetHand();
        dealer.resetHand();
        player.addCard(new Card(Suit.SPADES, Rank.TEN));
        player.addCard(new Card(Suit.HEARTS, Rank.SEVEN)); // сумма 17
        dealer.addCard(new Card(Suit.SPADES, Rank.KING));
        dealer.addCard(new Card(Suit.HEARTS, Rank.NINE)); // сумма 19
        result = (RoundResult) determineWinner.invoke(game);
        assertEquals(RoundResult.DEALER_WIN, result);

        // Ничья
        player.resetHand();
        dealer.resetHand();
        player.addCard(new Card(Suit.SPADES, Rank.TEN));
        player.addCard(new Card(Suit.HEARTS, Rank.SEVEN)); // сумма 17
        dealer.addCard(new Card(Suit.SPADES, Rank.TEN));
        dealer.addCard(new Card(Suit.HEARTS, Rank.SEVEN)); // сумма 17
        result = (RoundResult) determineWinner.invoke(game);
        assertEquals(RoundResult.DRAW, result);
    }

    @Test
    void testApplyResult() throws Exception {
        Method applyResult = Game.class.getDeclaredMethod("applyResult", RoundResult.class);
        applyResult.setAccessible(true);
        Player player = getPlayer();
        Dealer dealer = getDealer();

        applyResult.invoke(game, RoundResult.PLAYER_WIN);
        assertEquals(1, player.getWins());
        assertEquals(0, dealer.getWins());

        applyResult.invoke(game, RoundResult.DEALER_WIN);
        assertEquals(1, player.getWins());
        assertEquals(1, dealer.getWins());

        applyResult.invoke(game, RoundResult.DRAW);
        assertEquals(1, player.getWins());
        assertEquals(1, dealer.getWins());
    }

    @Test
    void testDealInitialCards() throws Exception {
        Method dealInitialCards = Game.class.getDeclaredMethod("dealInitialCards");
        dealInitialCards.setAccessible(true);
        testDeck.addCard(new Card(Suit.SPADES, Rank.TWO));
        testDeck.addCard(new Card(Suit.HEARTS, Rank.THREE));
        testDeck.addCard(new Card(Suit.DIAMONDS, Rank.FOUR));
        testDeck.addCard(new Card(Suit.CLUBS, Rank.FIVE));
        dealInitialCards.invoke(game);
        Player player = getPlayer();
        Dealer dealer = getDealer();
        assertEquals(2, player.getHand().size());
        assertEquals(2, dealer.getHand().size());
    }

    private Player getPlayer() throws Exception {
        Field playerField = Game.class.getDeclaredField("player");
        playerField.setAccessible(true);
        return (Player) playerField.get(game);
    }

    private Dealer getDealer() throws Exception {
        Field dealerField = Game.class.getDeclaredField("dealer");
        dealerField.setAccessible(true);
        return (Dealer) dealerField.get(game);
    }
}