package ru.nsu.oop.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.oop.cards.Card;
import ru.nsu.oop.cards.Rank;
import ru.nsu.oop.cards.Suit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    private TestDeck deck;
    private StubView view;
    private Game game;

    @BeforeEach
    void setUp() {
        deck = new TestDeck();
        view = new StubView();
        game = new Game(view, deck);
    }

    private static Card ten() {
        return new Card(Suit.SPADES, Rank.TEN);
    }

    private static Card card(Suit suit, Rank rank) {
        return new Card(suit, rank);
    }

    // ---------- start() ----------

    @Test
    void startShowsWelcome() {
        deck.addCards(ten(), ten(), ten(), ten());
        view.addPlayerAction(false);
        view.setThrowOnShowResult(true);

        assertThrows(RuntimeException.class, () -> game.start());

        assertTrue(view.getCalls().contains("showWelcome"));
    }

    @Test
    void startPlaysRoundsAndStopsAfterResult() {
        deck.addCards(ten(), ten(), ten(), ten());
        view.addPlayerAction(false);
        view.setThrowOnShowResult(true);

        assertThrows(RuntimeException.class, () -> game.start());

        assertTrue(view.getCalls().contains("showRoundStart 1"));
        assertTrue(view.getCalls().stream()
                .anyMatch(c -> c.startsWith("showResult ")));
    }

    @Test
    void startPlaysMultipleRounds() {
        // Первый раунд: P 20 : D 17 — победа игрока
        deck.addCards(
                card(Suit.SPADES, Rank.TEN),
                card(Suit.HEARTS, Rank.TEN),
                card(Suit.DIAMONDS, Rank.TEN),
                card(Suit.CLUBS, Rank.SEVEN),
                // Второй раунд — тот же расклад
                card(Suit.SPADES, Rank.TEN),
                card(Suit.HEARTS, Rank.TEN),
                card(Suit.DIAMONDS, Rank.TEN),
                card(Suit.CLUBS, Rank.SEVEN)
        );

        view.addPlayerAction(false); // ход игрока в 1-м раунде
        view.addPlayerAction(false); // ход игрока во 2-м раунде
        view.setThrowAfterResults(1); // упасть на втором showResult

        assertThrows(RuntimeException.class, () -> game.start());

        assertTrue(view.getCalls().contains("showRoundStart 1"),
                "первый раунд должен начаться");
        assertTrue(view.getCalls().contains("showRoundStart 2"),
                "второй раунд должен начаться");
        // Убедимся, что результат показан хотя бы один раз
        assertTrue(view.getCalls().stream()
                        .anyMatch(c -> c.startsWith("showResult ")),
                "результат первого раунда должен быть показан");
    }

    // ---------- Начисление побед ----------

    @Test
    void playerWinIncrementsPlayerScore() {
        deck.addCards(
                card(Suit.SPADES, Rank.TEN),
                card(Suit.HEARTS, Rank.TEN),
                card(Suit.DIAMONDS, Rank.TEN),
                card(Suit.CLUBS, Rank.SEVEN)
        );
        view.addPlayerAction(false);
        view.setThrowOnShowResult(true);

        assertThrows(RuntimeException.class, () -> game.start());

        assertEquals(1, game.getPlayer().getWins());
        assertEquals(0, game.getDealer().getWins());
    }

    @Test
    void dealerWinIncrementsDealerScore() {
        deck.addCards(
                card(Suit.SPADES, Rank.TEN),
                card(Suit.HEARTS, Rank.TEN),
                card(Suit.DIAMONDS, Rank.SEVEN),
                card(Suit.CLUBS, Rank.NINE)
        );
        view.addPlayerAction(false);
        view.setThrowOnShowResult(true);

        assertThrows(RuntimeException.class, () -> game.start());

        assertEquals(0, game.getPlayer().getWins());
        assertEquals(1, game.getDealer().getWins());
    }

    @Test
    void drawDoesNotIncrementScores() {
        deck.addCards(
                card(Suit.SPADES, Rank.TEN),
                card(Suit.HEARTS, Rank.TEN),
                card(Suit.DIAMONDS, Rank.SEVEN),
                card(Suit.CLUBS, Rank.SEVEN)
        );
        view.addPlayerAction(false);
        view.setThrowOnShowResult(true);

        assertThrows(RuntimeException.class, () -> game.start());

        assertEquals(0, game.getPlayer().getWins());
        assertEquals(0, game.getDealer().getWins());
    }

    @Test
    void scoreIsPassedToShowResult() {
        deck.addCards(
                card(Suit.SPADES, Rank.TEN),
                card(Suit.HEARTS, Rank.TEN),
                card(Suit.DIAMONDS, Rank.TEN),
                card(Suit.CLUBS, Rank.SEVEN)
        );
        view.addPlayerAction(false);
        view.setThrowOnShowResult(true);

        assertThrows(RuntimeException.class, () -> game.start());

        assertTrue(view.getCalls().stream()
                .anyMatch(c -> c.equals("showResult PLAYER_WIN 1:0")));
    }

    // ---------- applyResult ----------

    @Test
    void applyResultIncrementsOnlyPlayerOnPlayerWin() {
        game.applyResult(RoundResult.PLAYER_WIN);

        assertEquals(1, game.getPlayer().getWins());
        assertEquals(0, game.getDealer().getWins());
    }

    @Test
    void applyResultIncrementsOnlyDealerOnDealerWin() {
        game.applyResult(RoundResult.DEALER_WIN);

        assertEquals(0, game.getPlayer().getWins());
        assertEquals(1, game.getDealer().getWins());
    }

    @Test
    void applyResultLeavesScoresOnDraw() {
        game.applyResult(RoundResult.DRAW);

        assertEquals(0, game.getPlayer().getWins());
        assertEquals(0, game.getDealer().getWins());
    }
}