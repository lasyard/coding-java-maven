package io.github.lasyard.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApacheBeanUtilsTest {
    @Test
    public void testA() throws Exception {
        BeanA a = new BeanA();
        assertThat(BeanUtils.getProperty(a, "name")).isEqualTo("class A");
    }

    @Test
    public void testB() throws Exception {
        BeanB b = new BeanB();
        assertThat(BeanUtils.getProperty(b, "name")).isEqualTo("class B");
        assertThat(BeanUtils.getProperty(b, "long")).isEqualTo("1");
    }
}
