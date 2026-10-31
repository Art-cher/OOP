package ru.nsu.oop.parsers;



import ru.nsu.oop.expression.edges.Add;
import ru.nsu.oop.expression.edges.Div;
import ru.nsu.oop.expression.Expression;
import ru.nsu.oop.expression.edges.Mul;
import ru.nsu.oop.expression.leaves.Number;
import ru.nsu.oop.expression.edges.Sub;
import ru.nsu.oop.expression.leaves.Variable;

/**
 * Парсер математических выражений из строки.
 *
 * <p>Ожидается, что каждое выражение, кроме переменных и констант,
 * записано в скобках: например, {@code (3+(2*x))}. Переменные могут
 * иметь многобуквенные имена, константы — целые числа.</p>
 *
 * <p>Поддерживаемые операторы: {@code +}, {@code -}, {@code *}, {@code /}.</p>
 */
public class ExpressionParser {

    /**
     * Разбирает строку в дерево выражений.
     *
     * @param s строка с выражением
     * @return корень дерева выражений
     * @throws IllegalArgumentException если строка пуста, содержит
     *                                  несбалансированные скобки, неизвестный
     *                                  оператор или не является ни числом,
     *                                  ни переменной
     */
    public static Expression parse(String s) {
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException("Пустая строка выражения");
        }
        return parseAtom(s.trim());
    }

    private static Expression parseAtom(String s) {
        if (!isWrapped(s)) {
            if (containsOperatorOrBracket(s)) {
                throw new IllegalArgumentException(
                        "выражение: \"" + s + "\" не обернуто в скобки"
                );
            }
            return parseAtomValue(s);
        }

        String inner = s.substring(1, s.length() - 1);
        int op = findOperator(inner);

        if (op < 0) {
            throw new IllegalArgumentException("В выражении \"" + s + "\" не найден оператор");
        }

        Expression left = parseAtom(inner.substring(0, op).trim());
        Expression right = parseAtom(inner.substring(op + 1).trim());
        String sign = inner.substring(op, op + 1);

        return switch (sign) {
            case "+" -> new Add(left, right);
            case "-" -> new Sub(left, right);
            case "*" -> new Mul(left, right);
            case "/" -> new Div(left, right);
            default  -> throw new IllegalArgumentException("Неизвестный оператор: " + sign);
        };
    }

    private static Expression parseAtomValue(String s) {
        try {
            return new Number(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return new Variable(s);
        }
    }

    private static boolean isWrapped(String s) {
        if (s.length() < 2 || !s.startsWith("(") || !s.endsWith(")")) {
            return false;
        }

        int depth = 0;
        for (int i = 0; i < s.length(); i++) {
            String c = s.substring(i, i + 1);
            if (c.equals("(")) {
                depth++;
            } else if (c.equals(")")) {
                depth--;
                if (depth == 0) {
                    return i == s.length() - 1;
                }
            }
        }
        return false;
    }

    private static int findOperator(String s) {
        int depth = 0;
        for (int i = 0; i < s.length(); i++) {
            String c = s.substring(i, i + 1);
            if (c.equals("(")) {
                depth++;
            } else if (c.equals(")")) {
                depth--;
                if (depth < 0) {
                    throw new IllegalArgumentException("Несбалансированные скобки: \"" + s + "\"");
                }
            } else if (depth == 0 && isOperator(c)) {
                return i;
            }
        }
        if (depth != 0) {
            throw new IllegalArgumentException("Несбалансированные скобки: \"" + s + "\"");
        }
        return -1;
    }

    private static boolean isOperator(String c) {
        return c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/");
    }

    private static boolean containsOperatorOrBracket(String s) {
        return s.contains("(")
                || s.contains(")")
                || s.contains("+")
                || s.contains("-")
                || s.contains("*")
                || s.contains("/");
    }
}