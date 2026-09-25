package ru.nsu.oop.expression;

public class Div extends Expression {
    private final Expression left;
    private final Expression right;

    public Div(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void print() {

    }

    @Override
    public Expression derivative(String var) {
        return this;
    }

    @Override
    public int eval(String assignments) {
        return 0;
    }
}
