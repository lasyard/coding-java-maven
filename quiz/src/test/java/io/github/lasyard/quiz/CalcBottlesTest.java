package io.github.lasyard.quiz;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Slf4j
public class CalcBottlesTest {
    private static int fn(int num) {
        if (num >= 2) {
            return 4 * num - 5;
        } else if (num == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    public static Stream<Arguments> getArguments() {
        List<Arguments> list = new LinkedList<>();
        for (int i = 0; i <= 10; i++) {
            list.add(arguments(i, fn(i / 2)));
        }
        return list.stream();
    }

    @ParameterizedTest
    @MethodSource("getArguments")
    public void test(int input, int output) {
        log.info("{} yuan result in {} bottles totally.", input, output);
        assertThat(CalcBottles.calcBottles(input)).isEqualTo(output);
    }
}
