package ru.nsu.oop.expression;

import java.util.Map;

/**
 * Узел выражения, представляющий частное двух подвыражений: {@code (left / right)}.
 */
public class Div extends Expression {
    private final Expression left;
    private final Expression right;

    public Div(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left + "/" + right + ")";
    }

    @Override
    public Expression derivative(String var) {
        return new Div(
                new Sub(
                        new Mul(left.derivative(var), right),
                        new Mul(left, right.derivative(var))
                ),
                new Mul(right, right)
        );
    }

    @Override
    protected int eval(Map<String, Integer> vars) {
        return left.eval(vars) / right.eval(vars);
    }
}
