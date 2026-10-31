package ru.nsu.oop.expression.edges;

import ru.nsu.oop.expression.Expression;

import java.util.Map;

/**
 * Узел выражения, представляющий сумму двух подвыражений: {@code (left + right)}.
 */
public class Add extends Expression {
    private final Expression left;
    private final Expression right;

    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left + "+" + right + ")";
    }

    @Override
    public Expression derivative(String var) {
        return new Add(left.derivative(var), right.derivative(var));
    }

    @Override
    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) + right.eval(vars);
    }
}
