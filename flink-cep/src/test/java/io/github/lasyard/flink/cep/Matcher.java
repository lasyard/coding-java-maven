package io.github.lasyard.flink.cep;

import io.github.lasyard.flink.test.sink.TestSink;
import org.apache.flink.api.common.eventtime.IngestionTimeAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

final class Matcher {
    private Matcher() {
    }

    static void match(@Nonnull String input, Pattern<String, ?> pattern) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment()
            .setParallelism(1);
        env.getConfig().disableGenericTypes();
        // In flink 1.12.0, event time is default, so timestamp is must-have.
        DataStream<String> dataStream = env.fromCollection(Arrays.asList(input.split(" ")))
            .assignTimestampsAndWatermarks(
                WatermarkStrategy.<String>forMonotonousTimestamps()
                    .withTimestampAssigner(ctx -> new IngestionTimeAssigner<>())
            );
        PatternStream<String> patternStream = CEP.pattern(dataStream, pattern);
        DataStream<String> result = patternStream.select(new PatternSelectFunction<String, String>() {
            private static final long serialVersionUID = -5708246944590876484L;

            @Override
            public String select(Map<String, List<String>> map) {
                StringBuilder b = new StringBuilder();
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    for (String o : entry.getValue()) {
                        if (b.length() > 0) {
                            b.append(" ");
                        }
                        b.append(o);
                    }
                }
                return b.toString();
            }
        });
        result.addSink(new TestSink<>());
        env.execute();
    }
}
