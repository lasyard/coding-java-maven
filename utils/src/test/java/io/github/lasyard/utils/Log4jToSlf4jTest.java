package io.github.lasyard.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Log4jToSlf4jTest {
    private static final Logger logger = LogManager.getLogger(Log4jToSlf4jTest.class);

    @Test
    public void testLog4j2() {
        ByteArrayOutputStream str = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(str));
        logger.info("Message: {}", "Test - {}");
        System.setOut(old);
        System.out.println(str); // Actual output: "Message: Test - Test - {}\n"
        assertEquals("Message: Test - {}\n", str.toString());
    }
}
