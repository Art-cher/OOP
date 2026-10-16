package ru.nsu.oop.expression;

import java.util.Map;

/**
 * Узел выражения, представляющий произведение двух подвыражений: {@code (left * right)}.
 */
public class Mul extends Expression {
    private final Expression left;
    private final Expression right;

    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left + "*" + right + ")";
    }

    @Override
    public Expression derivative(String var) {
        return new Add(
                new Mul(left.derivative(var), right),
                new Mul(left, right.derivative(var))
        );
    }

    @Override
    protected int eval(Map<String, Integer> vars) {
        return left.eval(vars) * right.eval(vars);
    }
}
