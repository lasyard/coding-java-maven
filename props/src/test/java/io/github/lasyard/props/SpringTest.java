package io.github.lasyard.props;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Configuration
@PropertySource("classpath:test.properties")
public class SpringTest {
    private static SpringTest bean = null;

    @Value("${str1}")
    private String str1;
    @Value("${str2}")
    private String str2;
    @Value("${int}")
    private int anInt;
    @Value("${include}")
    private String include;
    @Value("${sub.str}")
    private String subStr;

    @BeforeAll
    public static void setupClass() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(SpringTest.class);
        ctx.refresh();
        bean = ctx.getBean(SpringTest.class);
    }

    @Test
    public void testSpringString() {
        assertEquals("abc", bean.str1);
    }

    @Test
    public void testSpringStringSubstitue() {
        assertEquals("abc is abc", bean.str2);
    }

    @Test
    public void testSpringInt() {
        assertEquals(3, bean.anInt);
    }

    @Test
    public void testSpringInclude() {
        assertEquals("sub.properties", bean.include);
        assertEquals("${sub.str}", bean.subStr);
    }
}
