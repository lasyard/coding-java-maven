package io.github.lasyard.antlr4.array.init;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArrayInitTest {
    @Test
    public void testAssign() {
        Node result = ArrayInit.assign("{1, {2, 3}, 4, {5}, 6}");
        assertThat(result.toString()).isEqualTo("{1, {2, 3}, 4, {5}, 6}");
    }
}
