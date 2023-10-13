package io.github.lasyard.quiz;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BigDecimalToStringTest {
    private static @NonNull Stream<Arguments> getParameters() {
        return Stream.of(
            arguments(new BigDecimal("10"), 0, "10"),
            arguments(new BigDecimal("1E1"), -1, "1E+1"),
            arguments(new BigDecimal("100.56"), 2, "100.56"),
            arguments(new BigDecimal("3.1415926"), 7, "3.1415926"),
            arguments(new BigDecimal("1E-7"), 7, "1E-7"),
            arguments(new BigDecimal("1E-6"), 6, "0.000001")
        );
    }

    @ParameterizedTest
    @MethodSource("getParameters")
    public void test(@NonNull BigDecimal data, int scale, String result) {
        assertThat(data.scale()).isEqualTo(scale);
        assertThat(data.toString()).isEqualTo(result);
    }
}
