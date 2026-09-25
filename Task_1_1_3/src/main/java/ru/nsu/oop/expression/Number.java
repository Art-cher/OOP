package ru.nsu.oop.expression;

public class Number extends Expression {
    private final int value;

    public Number(int value) {
        this.value = value;
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
        return value;
    }

}
