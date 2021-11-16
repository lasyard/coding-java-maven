package io.github.lasyard.flink.test.sink;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.assertj.core.api.ListAssert;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThat;

public class TestSink<T> implements SinkFunction<T> {
    private static final long serialVersionUID = 8363461737971569688L;

    // must be static
    private static final List<Object> values = new LinkedList<>();

    public static void clear() {
        values.clear();
    }

    public static List<Object> getValues() {
        return values;
    }

    @Nonnull
    public static ListAssert<Object> assertValues() {
        return assertThat(values);
    }

    @Override
    public synchronized void invoke(T value, Context ctx) {
        values.add(value);
    }
}
