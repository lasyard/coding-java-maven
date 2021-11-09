package io.github.lasyard.antlr4.array.init;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ValueNode implements Node {
    private final int value;

    @Override
    public String toString() {
        return "" + value;
    }
}
