package io.github.lasyard.props;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceBundleTest {
    private static ResourceBundle resourceBundle = null;

    @BeforeAll
    public static void setupClass() {
        resourceBundle = ResourceBundle.getBundle("test");
    }

    @Test
    public void testPropertiesString() {
        assertEquals("abc", resourceBundle.getString("str1"));
    }

    @Test
    public void testPropertiesStringSubstitute() {
        assertEquals("${str1} is abc", resourceBundle.getString("str2"));
    }

    @Test
    public void testPropertiesInt() {
        assertEquals(3, Integer.parseInt(resourceBundle.getString("int")));
    }

    @Test
    public void testPropertiesInclude() {
        assertEquals("sub.properties", resourceBundle.getString("include"));
        try {
            resourceBundle.getString("sub.str");
        } catch (MissingResourceException e) {
            assertEquals("sub.str", e.getKey());
        }
    }
}
