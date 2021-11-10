package io.github.lasyard.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanUtilsTest {
    @Test
    public void testA() {
        BeanA a = new BeanA();
        assertThat(BeanUtils.getPropertyValue(a, "name")).isEqualTo("class A");
    }

    @Test
    public void testB() {
        BeanB b = new BeanB();
        assertThat(BeanUtils.getPropertyValue(b, "name")).isEqualTo("class B");
        assertThat(BeanUtils.getPropertyValue(b, "long")).isEqualTo(1L);
    }
}
