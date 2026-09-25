package ru.nsu.oop.expression;

public abstract class Expression {

    public abstract void print();

    public abstract Expression derivative(String var);

    public abstract int eval(String assignments);

}
