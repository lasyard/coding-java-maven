package io.github.lasyard.props;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Configuration2Test {
    private static Configuration configuration = null;

    @BeforeAll
    public static void setupClass() throws ConfigurationException {
        Configurations configurations = new Configurations();
        configuration = configurations.properties(new File("test.properties"));
    }

    @Test
    public void testConfiguration2String() {
        assertEquals("abc", configuration.getString("str1"));
    }

    @Test
    public void testConfiguration2StringSubstitute() {
        assertEquals("abc is abc", configuration.getString("str2"));
    }

    @Test
    public void testConfiguration2Int() {
        assertEquals(3, configuration.getInt("int"));
    }

    @Test
    public void testConfiguration2Include() {
        assertEquals("sub.str", configuration.getString("sub.str"));
        assertNull(configuration.getString("include"));
    }
}
