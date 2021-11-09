package io.github.lasyard.flink.streaming;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public final class FlinkStreamingSocket {
    public static final int PORT = 12345;

    private FlinkStreamingSocket() {
    }

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().disableGenericTypes();
        DataStream<String> dataStream = env.socketTextStream("localhost", PORT, "\n");
        DataStream<Tuple2<String, Integer>> wordCount = dataStream
            .flatMap((String s, Collector<Tuple2<String, Integer>> collector) -> {
                for (String word : s.split("\\W+")) {
                    collector.collect(new Tuple2<>(word, 1));
                }
            })
            .returns(new TypeHint<Tuple2<String, Integer>>() {
            })
            .keyBy(t -> t.f0)
            .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
            .reduce((Tuple2<String, Integer> t1, Tuple2<String, Integer> t2) -> new Tuple2<>(
                t1.getField(0),
                (Integer) t1.getField(1) + (Integer) t2.getField(1)
            ));
        wordCount.print().setParallelism(1);
        env.execute();
    }
}
