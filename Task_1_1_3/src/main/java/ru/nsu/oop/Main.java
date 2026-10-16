package ru.nsu.oop;

import java.util.Scanner;
import ru.nsu.oop.expression.Expression;
import ru.nsu.oop.parsers.ExpressionParser;

/**
 * Точка входа консольного приложения для работы с математическими выражениями.
 *
 * <p>Программа последовательно запрашивает у пользователя:</p>
 * <ol>
 *     <li>выражение в скобочной форме;</li>
 *     <li>имя переменной для символьного дифференцирования;</li>
 *     <li>строку означивания переменных для вычисления значения.</li>
 * </ol>
 */
public class Main {

    /**
     * Запускает интерактивный режим работы с выражениями.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите выражение: ");
        String exprLine = scanner.nextLine().trim();

        Expression expression;
        try {
            expression = ExpressionParser.parse(exprLine);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка разбора выражения: " + e.getMessage());
            scanner.close();
            return;
        }
        System.out.println("Выражение: " + expression);

        System.out.print("Введите переменную для дифференцирования: ");
        String var = scanner.nextLine().trim();
        if (var.isEmpty()) {
            System.out.println("Имя переменной не задано, дифференцирование пропущено");
        } else {
            Expression derivative = expression.derivative(var);
            System.out.println("Производная по \"" + var + "\": " + derivative);
        }

        System.out.print("Введите означивание переменных (например, \"x = 10; y = 13\"): ");
        String varsLine = scanner.nextLine();
        try {
            int value = expression.eval(varsLine);
            System.out.println("Значение выражения: " + value);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка вычисления: " + e.getMessage());
        }

        scanner.close();
    }
}