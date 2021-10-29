package io.github.lasyard.quiz;

import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class ClasspathResourceTest {
    @Test
    public void testResourcePath() {
        URL resource = getClass().getResource("/name.txt");
        assertThat(resource).isNotNull();
        assertThat(resource.toString()).startsWith("file:");
        assertThat(resource.toString()).endsWith("/name.txt");
    }

    @Test
    public void testResourcePath1() {
        URL resource = getClass().getResource("name1.txt");
        assertThat(resource).isNotNull();
        assertThat(resource.toString()).startsWith("file:");
        assertThat(resource.toString()).endsWith("io/github/lasyard/quiz/name1.txt");
    }
}
