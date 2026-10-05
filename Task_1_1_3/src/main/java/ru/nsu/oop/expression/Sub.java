package ru.nsu.oop.expression;

import java.util.Map;

/**
 * Узел выражения, представляющий разность двух подвыражений: {@code (left - right)}.
 */
public class Sub extends Expression {
    private final Expression left;
    private final Expression right;

    public Sub(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left + "-" + right + ")";
    }

    @Override
    public Expression derivative(String var) {
        return new Sub(left.derivative(var), right.derivative(var));
    }

    @Override
    protected int eval(Map<String, Integer> vars) {
        return left.eval(vars) - right.eval(vars);
    }
}
