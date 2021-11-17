package io.github.lasyard.flink.sql;

import java.io.Serializable;

@FunctionalInterface
public interface TimeExtractor<T> extends Serializable {
    Long get(T event);
}
