package io.github.lasyard.antlr4.array.init;

import java.util.Arrays;
import java.util.stream.Collectors;

class ArrayNode implements Node {
    private final Node[] values;

    ArrayNode(int size) {
        values = new Node[size];
    }

    void set(int index, Node value) {
        values[index] = value;
    }

    @Override
    public String toString() {
        return "{" + Arrays.stream(values).map(Object::toString).collect(Collectors.joining(", ")) + "}";
    }
}
