package ru.nsu.oop.view;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.oop.cards.Card;
import ru.nsu.oop.cards.Hand;
import ru.nsu.oop.cards.Rank;
import ru.nsu.oop.cards.Suit;
import ru.nsu.oop.game.RoundResult;

class ConsoleViewTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testShowWelcome() {
        ConsoleView view = new ConsoleView();
        view.showWelcome();
        assertTrue(outContent.toString().contains("Добро пожаловать в Блэкджек!"));
    }

    @Test
    void testShowRoundStart() {
        ConsoleView view = new ConsoleView();
        view.showRoundStart(3);
        String output = outContent.toString();
        assertTrue(output.contains("Раунд 3"));
        assertTrue(output.contains("Дилер раздал карты"));
    }

    @Test
    void testShowPlayerHand() {
        ConsoleView view = new ConsoleView();
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        view.showPlayerHand(hand);
        String output = outContent.toString();
        assertTrue(output.contains("Ваши карты: [Туз Пики (11), Червовый король (10)] ⇒ 21"));
    }

    @Test
    void testShowDealerHandHidden() {
        ConsoleView view = new ConsoleView();
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.SPADES, Rank.TEN));
        hand.addCard(new Card(Suit.HEARTS, Rank.SIX));
        view.showDealerHand(hand, true);
        String output = outContent.toString();
        assertTrue(output.contains("Карты дилера: [Десятка Пики (10), <закрытая карта>]"));
    }

    @Test
    void testShowDealerHandOpen() {
        ConsoleView view = new ConsoleView();
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.SPADES, Rank.TEN));
        hand.addCard(new Card(Suit.HEARTS, Rank.SIX));
        view.showDealerHand(hand, false);
        String output = outContent.toString();
        assertTrue(output.contains("Карты дилера: [Десятка Пики (10), Шестёрка Червы (6)] ⇒ 16"));
    }

    @Test
    void testShowPlayerTurn() {
        ConsoleView view = new ConsoleView();
        view.showPlayerTurn();
        assertTrue(outContent.toString().contains("Ваш ход"));
    }

    @Test
    void testShowDealerTurn() {
        ConsoleView view = new ConsoleView();
        view.showDealerTurn();
        assertTrue(outContent.toString().contains("Ход дилера"));
    }

    @Test
    void testShowPlayerDrawsCard() {
        ConsoleView view = new ConsoleView();
        Card card = new Card(Suit.SPADES, Rank.TWO);
        view.showPlayerDrawsCard(card, 2);
        assertTrue(outContent.toString().contains("Вы открыли карту Двойка Пики (2)"));
    }

    @Test
    void testShowDealerDrawsCard() {
        ConsoleView view = new ConsoleView();
        Card card = new Card(Suit.HEARTS, Rank.THREE);
        view.showDealerDrawsCard(card, 3);
        assertTrue(outContent.toString().contains("Дилер открывает карту Тройка Червы (3)"));
    }

    @Test
    void testShowDealerOpensHiddenCard() {
        ConsoleView view = new ConsoleView();
        Card card = new Card(Suit.DIAMONDS, Rank.FOUR);
        view.showDealerOpensHiddenCard(card, 4);
        assertTrue(outContent.toString().contains("Дилер открывает закрытую карту Четвёрка Бубны (4)"));
    }

    @Test
    void testShowResultPlayerWin() {
        ConsoleView view = new ConsoleView();
        view.showResult(RoundResult.PLAYER_WIN, 2, 1);
        assertTrue(outContent.toString().contains("Вы выиграли раунд! Счет 2:1 в вашу пользу."));
    }

    @Test
    void testShowResultDealerWin() {
        ConsoleView view = new ConsoleView();
        view.showResult(RoundResult.DEALER_WIN, 1, 2);
        assertTrue(outContent.toString().contains("Дилер выиграл раунд. Счет 1:2."));
    }

    @Test
    void testShowResultDraw() {
        ConsoleView view = new ConsoleView();
        view.showResult(RoundResult.DRAW, 1, 1);
        assertTrue(outContent.toString().contains("Ничья. Счет 1:1."));
    }
}