package io.github.lasyard.utils;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class NestedEntityTest {
    @Test
    public void test() {
        List<EntityB> bs = new LinkedList<>();
        bs.add(new EntityB("Ba"));
        bs.add(new EntityB("Bb"));
        bs.add(new EntityB("Ba"));
        EntityA a = new EntityA(bs);
        Set<String> props = a.getPropertySet(EntityA.class, "name");
        assertThat(props).contains("Aa").doesNotContain("Ba", "Bb");
        props = a.getPropertySet(EntityB.class, "name");
        assertThat(props).doesNotContain("Aa").contains("Ba", "Bb");
    }
}
