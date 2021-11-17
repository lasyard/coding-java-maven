package io.github.lasyard.flink.sql;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Event extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 321266400526550339L;

    Event(int size) {
        super(size);
    }

    @Override
    public String toString() {
        return entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(e -> e.getKey() + ": " + e.getValue().toString())
            .collect(Collectors.joining(", "));
    }
}
