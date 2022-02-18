package io.github.lasyard.quiz;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class MapValueTest {
    @Test
    public void testValue() {
        Map<String, Integer> map = new HashMap<>();
        map.put("key", 1);
        Integer v = map.get("key");
        assertThat(v).isEqualTo(1);
        v = 2;
        assertThat(map.get("key")).isEqualTo(1);
        map.put("key", v);
        assertThat(map.get("key")).isEqualTo(2);
    }

    @Test
    public void testArray() {
        Map<String, Integer[]> map = new HashMap<>();
        map.put("key", new Integer[]{null});
        Integer[] v = map.get("key");
        assertThat(v[0]).isNull();
        v[0] = 1;
        assertThat(map.get("key")[0]).isEqualTo(1);
    }

    @Test
    public void testConcurrentArray() {
        Map<String, Integer[]> map = new ConcurrentHashMap<>();
        map.put("key", new Integer[]{null});
        Integer[] v = map.get("key");
        assertThat(v[0]).isNull();
        v[0] = 1;
        assertThat(map.get("key")[0]).isEqualTo(1);
    }

    @Test
    public void testMap() {
        Map<String, Map<String, Integer>> map = new HashMap<>();
        map.put("key", new HashMap<>());
        Map<String, Integer> v = map.get("key");
        assertThat(v.get("k")).isNull();
        v.put("k", 1);
        assertThat(map.get("key").get("k")).isEqualTo(1);
    }
}
