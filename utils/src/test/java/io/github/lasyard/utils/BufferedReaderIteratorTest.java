package io.github.lasyard.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class BufferedReaderIteratorTest {
    private static final String FILE = "/test.txt";

    @Test
    public void test() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(
            Objects.requireNonNull(getClass().getResourceAsStream(FILE)),
            StandardCharsets.UTF_8
        ));
        List<String> lines = Files.readAllLines(
            Paths.get(Objects.requireNonNull(getClass().getResource(FILE)).getPath()),
            StandardCharsets.UTF_8
        );
        int count = 0;
        for (String line : new BufferedReaderIterator(in)) {
            log.info(line);
            assertThat(line).isEqualTo(lines.get(count++));
        }
    }
}
