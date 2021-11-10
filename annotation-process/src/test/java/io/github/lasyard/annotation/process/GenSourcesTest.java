package io.github.lasyard.annotation.process;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class GenSourcesTest {
    @Test
    public void test() {
        NewClass newClass = new NewClass();
        String str = newClass.say();
        log.info(str);
        assertEquals(NewClass.class.getSimpleName(), str);
    }
}
