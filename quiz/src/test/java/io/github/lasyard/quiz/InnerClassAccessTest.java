package io.github.lasyard.quiz;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class InnerClassAccessTest {
    @Test
    public void test() {
        InnerClassAccess innerClassAccess = new InnerClassAccess();
        String str = innerClassAccess.getSecret();
        log.info(str);
        assertThat(str).isEqualTo(InnerClassAccess.ORIGINAL_SECRET);
        str = innerClassAccess.innerSay();
        log.info(str);
        assertThat(str).isEqualTo(InnerClassAccess.INNER_SAY);
        str = innerClassAccess.getSecret();
        log.info(str);
        assertThat(str).isEqualTo(InnerClassAccess.NEW_SECRET);
    }
}
