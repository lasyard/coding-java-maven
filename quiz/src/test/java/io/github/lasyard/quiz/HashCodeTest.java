package io.github.lasyard.quiz;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class HashCodeTest {
    private static @NonNull Stream<Arguments> getArguments() {
        return Stream.of(
            arguments(1, 1),
            arguments(2, 2),
            arguments(3L, 3),
            arguments(4L, 4),
            arguments(Integer.class, Integer.class.hashCode()),
            arguments(Long.class, Long.class.hashCode())
        );
    }

    @ParameterizedTest
    @MethodSource("getArguments")
    public void testHashCode(@NonNull Object obj, int hashCode) {
        assertThat(obj.hashCode()).isEqualTo(hashCode);
    }
}
