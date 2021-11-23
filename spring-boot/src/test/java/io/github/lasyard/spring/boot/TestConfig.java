package io.github.lasyard.spring.boot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {SpringBootWeb.class})
public class TestConfig {
    @Autowired
    private Config config;

    @Test
    public void test() {
        assertThat(config.getString()).isEqualTo("Test");
    }
}
