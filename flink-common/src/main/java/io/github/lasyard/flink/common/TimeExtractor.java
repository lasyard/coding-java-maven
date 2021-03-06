package io.github.lasyard.flink.common;

import java.io.Serializable;

@FunctionalInterface
public interface TimeExtractor<T> extends Serializable {
    Long get(T event);
}
