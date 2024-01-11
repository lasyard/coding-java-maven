package io.github.lasyard.quiz;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CatchExceptionInStreamTest {
    @Test
    public void testCatch() {
        boolean exceptionCaught = false;
        try {
            Arrays.asList(1, 2, 3)
                .forEach(x -> {
                    System.out.println(x);
                    if (x == 2) {
                        throw new BreakException("x == 2");
                    }
                });
        } catch (BreakException e) {
            exceptionCaught = true;
            assertThat(e.getMessage()).isEqualTo("x == 2");
        }
        assertTrue(exceptionCaught);
    }

    private static class BreakException extends RuntimeException {
        private static final long serialVersionUID = 2756240103043966868L;

        public BreakException(String msg) {
            super(msg);
        }
    }
}
