package io.github.lasyard.quiz.proxy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceTest {
    private static Service proxy;

    @BeforeAll
    public static void setupAll() {
        proxy = Service.proxy(ServiceImpl.INSTANCE);
    }

    @Test
    public void testNum() {
        assertThat(proxy.num()).isEqualTo(ServiceImpl.INSTANCE.num() + 1);
    }
}
