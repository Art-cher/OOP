package ru.nsu.oop.expression.leaves;

import ru.nsu.oop.expression.Expression;

import java.util.Map;

/**
 * Узел выражения, представляющий переменную с именем.
 *
 * <p>Имя переменной может быть многобуквенным.</p>
 */
public class Variable extends Expression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Expression derivative(String var) {
        return new Number(name.equals(var) ? 1 : 0);
    }

    @Override
    public int eval(Map<String, Integer> vars) {
        Integer value = vars.get(name);
        if (value == null) {
            throw new IllegalArgumentException("Переменная " + name + " не определена");
        }
        return value;
    }
}
