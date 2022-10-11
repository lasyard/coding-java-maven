package io.github.lasyard.quiz;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Slf4j
public class CalcBitsTest {
    private static @NonNull Stream<Arguments> getArguments() {
        return Stream.of(
            arguments(0, 0),
            arguments(1, 1),
            arguments(2, 1),
            arguments(3, 2),
            arguments(4, 1),
            arguments(5, 2),
            arguments(6, 2),
            arguments(7, 3),
            arguments(8, 1),
            arguments(9, 2)
        );
    }

    @ParameterizedTest
    @MethodSource("getArguments")
    public void testCalcBits(int input, int output) {
        log.info("Number {} has {} bits set to 1.", input, output);
        assertThat(CalcBits.calcBits(input)).isEqualTo(output);
    }

    @ParameterizedTest
    @MethodSource("getArguments")
    public void testCalcBitsRecursive(int input, int output) {
        log.info("Number {} has {} bits set to 1.", input, output);
        assertThat(CalcBits.calcBitsRecursive(input)).isEqualTo(output);
    }
}
