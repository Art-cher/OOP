package ru.nsu.oop;

import java.util.Scanner;

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
                sb.append(formatCard(hand.getCard(i), hand.getDisplayValue(i)));
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
            sb.append(formatCard(hand.getCard(i), hand.getDisplayValue(i)));
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
        System.out.println("Вы открыли карту " + formatCard(card, displayValue));
    }

    @Override
    public void showDealerDrawsCard(Card card, int displayValue) {
        System.out.println("Дилер открывает карту " + formatCard(card, displayValue));
    }

    @Override
    public void showDealerOpensHiddenCard(Card card, int displayValue) {
        System.out.println("Дилер открывает закрытую карту " + formatCard(card, displayValue));
    }

    @Override
    public boolean askPlayerAction() {
        System.out.println("Введите \"1\", чтобы взять карту, и \"0\", чтобы остановиться...");
        return scanner.nextInt() == 1;
    }

    @Override
    public void showResult(RoundResult result, int playerWins, int dealerWins) {
        switch (result) {
            case PLAYER_WIN ->
                    System.out.println("Вы выиграли раунд! Счет "
                            + playerWins + ":" + dealerWins + " в вашу пользу.");
            case DEALER_WIN ->
                    System.out.println("Дилер выиграл раунд. Счет "
                            + playerWins + ":" + dealerWins + ".");
            case DRAW ->
                    System.out.println("Ничья. Счет "
                            + playerWins + ":" + dealerWins + ".");
            default -> {
                // Все варианты RoundResult обработаны; ветка нужна для checkstyle.
            }
        }
    }

    // ============ Форматирование ============

    /**
     * Возвращает текст одной карты в формате ТЗ: «Пиковая дама (10)».
     *
     * @param card         карта
     * @param displayValue значение карты для отображения
     * @return строковое представление карты
     */
    private String formatCard(Card card, int displayValue) {
        Rank rank = card.rank();
        Suit suit = card.suit();
        String name = switch (rank) {
            case QUEEN -> suitFeminine(suit) + " " + rankName(rank).toLowerCase();
            case KING -> suitMasculine(suit) + " " + rankName(rank).toLowerCase();
            default -> rankName(rank) + " " + suitBase(suit);
        };
        return name + " (" + displayValue + ")";
    }

    /**
     * Возвращает русское название достоинства карты.
     *
     * @param r достоинство
     * @return название достоинства
     */
    private String rankName(Rank r) {
        return switch (r) {
            case TWO -> "Двойка";
            case THREE -> "Тройка";
            case FOUR -> "Четвёрка";
            case FIVE -> "Пятёрка";
            case SIX -> "Шестёрка";
            case SEVEN -> "Семёрка";
            case EIGHT -> "Восьмёрка";
            case NINE -> "Девятка";
            case TEN -> "Десятка";
            case JACK -> "Валет";
            case QUEEN -> "Дама";
            case KING -> "Король";
            case ACE -> "Туз";
        };
    }

    /**
     * Возвращает базовое имя масти: «Пики», «Червы», «Бубны», «Трефы».
     *
     * @param s масть
     * @return название масти в форме существительного
     */
    private String suitBase(Suit s) {
        return switch (s) {
            case SPADES -> "Пики";
            case HEARTS -> "Червы";
            case DIAMONDS -> "Бубны";
            case CLUBS -> "Трефы";
        };
    }

    /**
     * Возвращает прилагательное женского рода для масти: «Пиковая», «Червовая».
     *
     * @param s масть
     * @return прилагательное женского рода
     */
    private String suitFeminine(Suit s) {
        return switch (s) {
            case SPADES -> "Пиковая";
            case HEARTS -> "Червовая";
            case DIAMONDS -> "Бубновая";
            case CLUBS -> "Трефовая";
        };
    }

    /**
     * Возвращает прилагательное мужского рода для масти: «Пиковый», «Червовый».
     *
     * @param s масть
     * @return прилагательное мужского рода
     */
    private String suitMasculine(Suit s) {
        return switch (s) {
            case SPADES -> "Пиковый";
            case HEARTS -> "Червовый";
            case DIAMONDS -> "Бубновый";
            case CLUBS -> "Трефовый";
        };
    }
}