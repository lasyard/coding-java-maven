package io.github.lasyard.i18n;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.assertj.core.api.Assertions.assertThat;

public class TestI18n {
    @BeforeAll
    public static void setupClass() {
        Locale.setDefault(Locale.ROOT);
    }

    @Test
    public void testDefault() {
        ResourceBundle bundle = ResourceBundle.getBundle("message");
        assertThat(bundle.getString("name")).isEqualTo("xxx");
    }

    @Test
    public void testZhCn() {
        ResourceBundle bundle = ResourceBundle.getBundle("message", Locale.CHINA);
        assertThat(bundle.getString("name")).isEqualTo("叉叉叉");
    }
}
