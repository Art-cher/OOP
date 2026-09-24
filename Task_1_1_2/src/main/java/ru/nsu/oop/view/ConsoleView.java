package ru.nsu.oop.view;

import java.util.Scanner;

import ru.nsu.oop.cards.Card;
import ru.nsu.oop.cards.Hand;
import ru.nsu.oop.game.RoundResult;

/**
 * Консольная реализация представления.
 * Весь русский текст живёт здесь.
 */
public class ConsoleView implements View {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void showWelcome() {
        System.out.println("Добро пожаловать в Блэкджек!");
    }

    @Override
    public void showRoundStart(int roundNumber) {
        System.out.println("Раунд " + roundNumber + "\nДилер раздал карты");
    }

    @Override
    public void showPlayerHand(Hand hand) {
        System.out.println("    Ваши карты: " + formatHand(hand));
    }

    @Override
    public void showDealerHand(Hand hand, boolean hideCard) {
        StringBuilder sb = new StringBuilder("    Карты дилера: [");
        for (int i = 0; i < hand.size(); i++) {
            if (i == 1 && hideCard) {
                sb.append("<закрытая карта>");
            } else {
                sb.append(hand.getCard(i).toString(hand.getDisplayValue(i)));
            }
            if (i < hand.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        if (!hideCard) {
            sb.append(" ⇒ ").append(hand.getSum());
        }
        System.out.println(sb + "\n");
    }

    /**
     * Форматирует руку игрока целиком вместе с итоговой суммой.
     *
     * @param hand рука
     * @return строка вида {@code [Туз Пики (11), ...] ⇒ 21}
     */
    private String formatHand(Hand hand) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < hand.size(); i++) {
            sb.append(hand.getCard(i).toString(hand.getDisplayValue(i)));
            if (i < hand.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("] ⇒ ").append(hand.getSum());
        return sb.toString();
    }

    @Override
    public void showPlayerTurn() {
        System.out.println("Ваш ход\n-------");
    }

    @Override
    public void showDealerTurn() {
        System.out.println("Ход дилера\n-------");
    }

    @Override
    public void showPlayerDrawsCard(Card card, int displayValue) {
        System.out.println("Вы открыли карту " + card.toString(displayValue));
    }

    @Override
    public void showDealerDrawsCard(Card card, int displayValue) {
        System.out.println("Дилер открывает карту " + card.toString(displayValue));
    }

    @Override
    public void showDealerOpensHiddenCard(Card card, int displayValue) {
        System.out.println("Дилер открывает закрытую карту " + card.toString(displayValue));
    }

    @Override
    public boolean askPlayerAction() {
        System.out.println("Введите \"1\", чтобы взять карту, и \"0\", чтобы остановиться...");
        return scanner.nextInt() == 1;
    }

    @Override
    public void showResult(RoundResult result, int playerWins, int dealerWins) {
        switch (result) {
            case PLAYER_WIN -> System.out.println("Вы выиграли раунд! Счет "
                    + playerWins + ":" + dealerWins + " в вашу пользу.");
            case DEALER_WIN -> System.out.println("Дилер выиграл раунд. Счет "
                    + playerWins + ":" + dealerWins + ".");
            case DRAW -> System.out.println("Ничья. Счет "
                    + playerWins + ":" + dealerWins + ".");
            default -> {
                // Все варианты RoundResult обработаны; ветка нужна для checkstyle.
            }
        }
    }
}