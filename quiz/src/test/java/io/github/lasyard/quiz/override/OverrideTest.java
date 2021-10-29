package io.github.lasyard.quiz.override;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class OverrideTest {
    @Test
    public void test() {
        Base base = new Derived();
        String str = base.say();
        log.info(str);
        assertThat(str).isEqualTo(Derived.SAY);
        @SuppressWarnings("CastCanBeRemovedNarrowingVariableType")
        String str1 = ((Derived) base).saySomethingElse();
        log.info(str1);
        assertThat(str1).isEqualTo(Derived.SAY_ELSE);
    }

    @Test
    public void test2() {
        Base base = new Base();
        String str = base.say();
        log.info(str);
        assertThat(str).isEqualTo(Base.SAY);
    }

    @Test
    public void test3() {
        assertThrows(ClassCastException.class, () -> {
            Base base = new Base();
            @SuppressWarnings("ConstantConditions")
            String str = ((Derived) base).saySomethingElse();
            log.info(str);
        });
    }
}
