package io.github.lasyard.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringUtilsTest {
    @Test
    public void test() {
        assertThat(StringUtils.uppercaseFirst("abc")).isEqualTo("Abc");
    }
}
