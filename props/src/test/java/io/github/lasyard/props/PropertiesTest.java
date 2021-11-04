package io.github.lasyard.props;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PropertiesTest {
    private static final Properties properties = new Properties();

    @BeforeAll
    public static void setupClass() throws IOException {
        // Use absolute path here to search the root of classpath.
        properties.load(PropertiesTest.class.getResourceAsStream("/test.properties"));
    }

    @Test
    public void testPropertiesString() {
        assertEquals("abc", properties.getProperty("str1"));
    }

    @Test
    public void testPropertiesStringSubstitute() {
        assertEquals("${str1} is abc", properties.getProperty("str2"));
    }

    @Test
    public void testPropertiesInt() {
        assertEquals(3, Integer.parseInt(properties.getProperty("int")));
    }

    @Test
    public void testPropertiesInclude() {
        assertEquals("sub.properties", properties.getProperty("include"));
        assertNull(properties.getProperty("sub.str"));
    }
}
