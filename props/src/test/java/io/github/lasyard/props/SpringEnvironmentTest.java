package io.github.lasyard.props;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Configuration
@PropertySource("classpath:test.properties")
public class SpringEnvironmentTest {
    private static SpringEnvironmentTest bean = null;

    @Autowired
    private Environment env;

    @BeforeAll
    public static void setupClass() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(SpringEnvironmentTest.class);
        ctx.refresh();
        bean = ctx.getBean(SpringEnvironmentTest.class);
    }

    @Test
    public void testSpringString() {
        assertEquals("abc", bean.env.getProperty("str1"));
    }

    @Test
    public void testSpringStringSubstitute() {
        assertEquals("abc is abc", bean.env.getProperty("str2"));
    }

    @Test
    public void testSpringInt() {
        assertEquals(3, Integer.parseInt(Objects.requireNonNull(bean.env.getProperty("int"))));
    }

    @Test
    public void testSpringInclude() {
        assertEquals("sub.properties", bean.env.getProperty("include"));
        assertNull(bean.env.getProperty("sub.str"));
    }
}
