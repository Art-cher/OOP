package ru.nsu.oop.expression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import ru.nsu.oop.expression.edges.Add;
import ru.nsu.oop.expression.edges.Div;
import ru.nsu.oop.expression.edges.Mul;
import ru.nsu.oop.expression.edges.Sub;
import ru.nsu.oop.expression.leaves.Number;
import ru.nsu.oop.expression.leaves.Variable;
import ru.nsu.oop.parsers.ExpressionParser;

/**
 * Тесты для иерархии {@link Expression} и парсера выражений.
 */
class ExpressionTest {

    // ---------- toString / print ----------

    @Test
    void numberToString() {
        assertEquals("42", new ru.nsu.oop.expression.leaves.Number(42).toString());
    }

    @Test
    void variableToString() {
        assertEquals("x", new Variable("x").toString());
    }

    @Test
    void complexExpressionToString() {
        Expression e = new Add(new ru.nsu.oop.expression.leaves.Number(3), new Mul(new ru.nsu.oop.expression.leaves.Number(2), new Variable("x")));
        assertEquals("(3+(2*x))", e.toString());
    }

    @Test
    void printToStream() {
        Expression e = new Add(new ru.nsu.oop.expression.leaves.Number(3), new ru.nsu.oop.expression.leaves.Number(4));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(buffer);
        e.print(stream);
        stream.flush();
        assertTrue(buffer.toString().startsWith("(3+4)"));
    }

    // ---------- derivative ----------

    @Test
    void numberDerivativeIsZero() {
        assertEquals("0", new ru.nsu.oop.expression.leaves.Number(5).derivative("x").toString());
    }

    @Test
    void variableDerivativeMatches() {
        assertEquals("1", new Variable("x").derivative("x").toString());
    }

    @Test
    void variableDerivativeDoesNotMatch() {
        assertEquals("0", new Variable("x").derivative("y").toString());
    }

    @Test
    void addDerivative() {
        // (x + y)' по x = (1 + 0)
        Expression e = new Add(new Variable("x"), new Variable("y"));
        assertEquals("(1+0)", e.derivative("x").toString());
    }

    @Test
    void subDerivative() {
        // (x - y)' по x = (1 - 0)
        Expression e = new Sub(new Variable("x"), new Variable("y"));
        assertEquals("(1-0)", e.derivative("x").toString());
    }

    @Test
    void mulDerivativeProductRule() {
        // (2 * x)' по x = (0 * x) + (2 * 1)
        Expression e = new Mul(new ru.nsu.oop.expression.leaves.Number(2), new Variable("x"));
        assertEquals("((0*x)+(2*1))", e.derivative("x").toString());
    }

    @Test
    void divDerivativeQuotientRule() {
        // (x / 2)' по x = ((1 * 2) - (x * 0)) / (2 * 2)
        Expression e = new Div(new Variable("x"), new ru.nsu.oop.expression.leaves.Number(2));
        assertEquals("(((1*2)-(x*0))/(2*2))", e.derivative("x").toString());
    }

    @Test
    void derivativeDoesNotMutateOriginal() {
        Expression e = new Add(new ru.nsu.oop.expression.leaves.Number(3), new Mul(new ru.nsu.oop.expression.leaves.Number(2), new Variable("x")));
        e.derivative("x");
        assertEquals("(3+(2*x))", e.toString());
    }

    @Test
    void derivativeFromAssignment() {
        // Из условия: e = (3+(2*x)), de/dx = (0 + ((0*x) + (2*1)))
        Expression e = new Add(new ru.nsu.oop.expression.leaves.Number(3), new Mul(new ru.nsu.oop.expression.leaves.Number(2), new Variable("x")));
        assertEquals("(0+((0*x)+(2*1)))", e.derivative("x").toString());
    }

    // ---------- eval ----------

    @Test
    void evalConstant() {
        assertEquals(7, new ru.nsu.oop.expression.leaves.Number(7).eval(""));
    }

    @Test
    void evalVariable() {
        assertEquals(10, new Variable("x").eval("x = 10"));
    }

    @Test
    void evalExampleFromAssignment() {
        Expression e = new Add(new ru.nsu.oop.expression.leaves.Number(3), new Mul(new ru.nsu.oop.expression.leaves.Number(2), new Variable("x")));
        assertEquals(23, e.eval("x = 10; y = 13"));
    }

    @Test
    void evalAllOperations() {
        // ((10 + 5) - 3) * (8 / 4) = 12 * 2 = 24
        Expression e = new Mul(
                new Sub(new Add(new ru.nsu.oop.expression.leaves.Number(10), new ru.nsu.oop.expression.leaves.Number(5)), new ru.nsu.oop.expression.leaves.Number(3)),
                new Div(new ru.nsu.oop.expression.leaves.Number(8), new ru.nsu.oop.expression.leaves.Number(4))
        );
        assertEquals(24, e.eval(""));
    }

    @Test
    void evalMissingVariableThrows() {
        Expression e = new Add(new Variable("x"), new ru.nsu.oop.expression.leaves.Number(1));
        assertThrows(IllegalArgumentException.class, () -> e.eval("y = 5"));
    }

    @Test
    void evalEmptyVarsOnConstantExpression() {
        Expression e = new Add(new ru.nsu.oop.expression.leaves.Number(1), new Number(2));
        assertEquals(3, e.eval((String) null));
    }

    // ---------- ExpressionParser ----------

    @Test
    void parseNumber() {
        assertEquals("5", ExpressionParser.parse("5").toString());
    }

    @Test
    void parseVariable() {
        assertEquals("x", ExpressionParser.parse("x").toString());
    }

    @Test
    void parseMultiLetterVariable() {
        assertEquals("alpha", ExpressionParser.parse("alpha").toString());
    }

    @Test
    void parseAddition() {
        assertEquals("(1+2)", ExpressionParser.parse("(1+2)").toString());
    }

    @Test
    void parseAllOperators() {
        assertEquals("(1+2)", ExpressionParser.parse("(1+2)").toString());
        assertEquals("(1-2)", ExpressionParser.parse("(1-2)").toString());
        assertEquals("(1*2)", ExpressionParser.parse("(1*2)").toString());
        assertEquals("(1/2)", ExpressionParser.parse("(1/2)").toString());
    }

    @Test
    void parseNestedExpression() {
        Expression e = ExpressionParser.parse("(3+(2*x))");
        assertEquals("(3+(2*x))", e.toString());
    }

    @Test
    void parseRoundTrip() {
        String src = "((a+b)*(c-(d/e)))";
        assertEquals(src, ExpressionParser.parse(src).toString());
    }

    @Test
    void parseWhitespace() {
        assertEquals("(3+(2*x))", ExpressionParser.parse("  ( 3 + ( 2 * x ) )  ").toString());
    }

    @Test
    void parseEmptyStringThrows() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse(""));
    }

    @Test
    void parseNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse(null));
    }

    @Test
    void parseBlankStringThrows() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("   "));
    }

    @Test
    void parseUnbalancedBracketsThrows() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("((1+2)"));
    }

    @Test
    void parseWithoutOperatorInsideBracketsThrows() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("(1)"));
    }

    @Test
    void parseAdjacentExpressionsThrows() {
        // (3+2)*(5) — не является цельным выражением в скобках
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("(3+2)*(5)"));
    }

    @Test
    void parseBareOperatorExpressionThrows() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("3+2"));
    }
}