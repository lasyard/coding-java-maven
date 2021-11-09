package io.github.lasyard.antlr4.simple.expr;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleExprTest {
    @Test
    public void test1() {
        assertThat(SimpleExpr.eval("3 + 5;")).isEqualTo(8);
    }

    @Test
    public void test2() {
        assertThat(SimpleExpr.eval("a = 3; b = 4; 3 + a*b\n")).isEqualTo(15);
    }

    @Test
    public void test3() {
        assertThat(SimpleExpr.eval("a = 2; b = 3; (a + 2)*b;")).isEqualTo(12);
    }
}
