package io.github.lasyard.flink.cep;

import org.apache.flink.cep.pattern.conditions.SimpleCondition;

class Condition extends SimpleCondition<String> {
    private static final long serialVersionUID = 8430169955230929953L;

    private final String str;

    Condition(String str) {
        this.str = str;
    }

    @Override
    public boolean filter(String str) {
        return str != null && str.startsWith(this.str);
    }
}
