package ru.nsu.oop.expression;

import java.io.PrintStream;
import java.util.Map;
import ru.nsu.oop.parsers.VarsParser;

/**
 * Абстрактный корень иерархии математических выражений.
 *
 * <p>Каждый узел дерева выражений является наследником этого класса и
 * реализует три операции:</p>
 * <ul>
 *     <li>{@link #derivative(String)} — символьное дифференцирование;</li>
 *     <li>{@link #eval(Map)} — вычисление при заданном означивании переменных;</li>
 *     <li>{@link #toString()} — печать выражения в скобочной форме.</li>
 * </ul>
 *
 * <p>Конкретные наследники: {@link Number}, {@link Variable},
 * {@link Add}, {@link Sub}, {@link Mul}, {@link Div}.</p>
 */
public abstract class Expression {

    public void print(PrintStream out) {
        out.println(this);
    }

    public void print() {
        print(System.out);
    }

    public abstract Expression derivative(String var);

    public int eval(String vars) {
        return eval(VarsParser.parseVars(vars));
    }

    protected abstract int eval(Map<String, Integer> vars);
}
