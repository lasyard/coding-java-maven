package io.github.lasyard.kafka.wc;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public final class KafkaWordCount {
    private static final String BOOTSTRAP_SERVERS = "las1:9092";

    private KafkaWordCount() {
    }

    public static void main(String[] args) {
        final StreamsBuilder builder = new StreamsBuilder();
        builder.<String, String>stream("streams-input")
            .flatMapValues(value -> Arrays.asList(value.split("\\W+")))
            .groupBy((key, value) -> value)
            .count(Materialized.as("count-store"))
            .toStream()
            .map((key, value) -> new KeyValue<>(key, key + ": " + value))
            .to("streams-output", Produced.with(Serdes.String(), Serdes.String()));
        final Topology topology = builder.build();
        System.out.println(topology.describe());
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-word-count");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        final KafkaStreams streams = new KafkaStreams(topology, props);
        final CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });
        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
