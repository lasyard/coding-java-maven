package io.github.lasyard.flink.cep;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public final class KafkaCep {
    private static final String BOOTSTRAP_SERVER = "localhost:9092";

    private KafkaCep() {
    }

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        KafkaSource<String> source = KafkaSource.<String>builder()
            .setBootstrapServers(BOOTSTRAP_SERVER)
            .setTopics("test")
            .setGroupId("test")
            .setStartingOffsets(OffsetsInitializer.earliest())
            .setValueOnlyDeserializer(new SimpleStringSchema())
            .build();
        // Watermark is crucial for CEP.
        DataStream<String> dataStream = env
            .fromSource(source, WatermarkStrategy.forMonotonousTimestamps(), "Kafka")
            .setParallelism(3);
        KeyedStream<String, String> keyed = dataStream.keyBy(value -> value.substring(0, 1));
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new SimpleCondition<String>() {
            private static final long serialVersionUID = 5132932755286546969L;

            @Override
            public boolean filter(String value) {
                if (value.length() >= 2) {
                    return value.charAt(1) == 'a';
                }
                return false;
            }
        }).times(2);
        PatternStream<String> patternStream = CEP.pattern(keyed, pattern);
        // The parallelism can only be 1 if the input stream is not keyed.
        DataStream<String> out = patternStream.select(map -> map.get("1").get(0)).setParallelism(5);
        KafkaSink<String> sink = KafkaSink.<String>builder()
            .setBootstrapServers(BOOTSTRAP_SERVER)
            .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                .setTopic("test-out")
                .setValueSerializationSchema(new SimpleStringSchema())
                .build())
            .setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
            .build();
        out.sinkTo(sink).setParallelism(3);
        env.execute();
    }
}
