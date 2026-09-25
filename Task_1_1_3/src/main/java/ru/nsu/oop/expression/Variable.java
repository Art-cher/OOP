package ru.nsu.oop.expression;

public class Variable extends Expression {
    private final String name;

    public Variable(String name) {
        this.name = name;
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
