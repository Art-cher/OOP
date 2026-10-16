package ru.nsu.oop.expression;

import java.util.Map;

/**
 * Узел выражения, представляющий целочисленную константу.
 */
public class Number extends Expression {
    private final int value;

    public Number(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public Expression derivative(String var) {
        return new Number(0);
    }

    @Override
    protected int eval(Map<String, Integer> vars) {
        return value;
    }

}
