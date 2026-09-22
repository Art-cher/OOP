package ru.nsu.oop;

/**
 * Базовый класс участника игры (игрока или дилера).
 * Хранит руку, счёт побед и общие для всех участников операции.
 */
public abstract class Participant {
    /** Рука участника. */
    protected final Hand hand;

    /** Количество выигранных раундов. */
    protected int wins;

    /**
     * Создаёт участника с пустой рукой и нулевым счётом.
     */
    public Participant() {
        this.hand = new Hand();
        this.wins = 0;
    }

    /**
     * Добавляет карту в руку участника.
     *
     * @param card карта, которую нужно добавить
     */
    public void addCard(Card card) {
        hand.addCard(card);
    }

    /**
     * Возвращает руку участника.
     *
     * @return текущая рука
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Возвращает количество выигранных раундов.
     *
     * @return число побед
     */
    public int getWins() {
        return wins;
    }

    /**
     * Увеличивает счёт побед на единицу.
     */
    public void incrementWins() {
        wins++;
    }

    /**
     * Очищает руку участника, начиная новый раунд.
     */
    public void resetHand() {
        hand.clear();
    }

    /**
     * Проверяет, собрал ли участник блэкджек.
     *
     * @return {@code true}, если у участника блэкджек
     */
    public boolean isBlackjack() {
        return hand.isBlackjack();
    }

    /**
     * Проверяет, перебрал ли участник (сумма карт больше 21).
     *
     * @return {@code true}, если сумма карт больше 21
     */
    public boolean isBust() {
        return hand.isBust();
    }
}