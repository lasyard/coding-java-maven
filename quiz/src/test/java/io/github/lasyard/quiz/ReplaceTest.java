package io.github.lasyard.quiz;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReplaceTest {
    @Test
    public void testReplaceAll() {
        String s = "Map<String, ?>";
        assertThat(s.replaceAll("<.*>", "")).isEqualTo("Map");
    }
}
