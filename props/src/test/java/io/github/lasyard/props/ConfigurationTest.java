package io.github.lasyard.props;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ConfigurationTest {
    private static Configuration configuration = null;

    @BeforeAll
    public static void setupClass() throws ConfigurationException {
        configuration = new PropertiesConfiguration("test.properties");
    }

    @Test
    public void testConfigurationString() {
        assertEquals("abc", configuration.getString("str1"));
    }

    @Test
    public void testConfigurationStringSubstitute() {
        assertEquals("abc is abc", configuration.getString("str2"));
    }

    @Test
    public void testConfigurationInt() {
        assertEquals(3, configuration.getInt("int"));
    }

    @Test
    public void testConfigurationInclude() {
        assertEquals("sub.str", configuration.getString("sub.str"));
        assertNull(configuration.getString("include"));
    }
}
