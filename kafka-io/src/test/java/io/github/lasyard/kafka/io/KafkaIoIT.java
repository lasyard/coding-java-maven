package io.github.lasyard.kafka.io;

import io.github.lasyard.kafka.admin.KafkaAdmin;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class KafkaIoIT {
    private static final String BOOTSTRAP_SERVERS = "las1:9092";
    private static final KafkaAdmin admin = new KafkaAdmin(BOOTSTRAP_SERVERS);

    private static String topic = null;

    @BeforeAll
    public static void setupAll() {
        topic = RandomStringUtils.randomAlphabetic(8);
        admin.createTopic(topic, 3, (short) 1);
    }

    @AfterAll
    public static void tearDownAll() {
        admin.deleteTopic(topic);
    }

    @Test
    public void testTxRx() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future<List<ConsumerRecord<Integer, String>>> records = executor.submit(() -> {
            Properties consumerProps = new Properties();
            consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, admin.getBootstrapServers());
            consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "one");
            consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
            consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
            try (Consumer<Integer, String> consumer = new KafkaConsumer<>(consumerProps)) {
                consumer.subscribe(Collections.singletonList(topic));
                List<ConsumerRecord<Integer, String>> recordList = new LinkedList<>();
                while (recordList.size() < 10) {
                    ConsumerRecords<Integer, String> recs = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<Integer, String> rec : recs) {
                        recordList.add(rec);
                    }
                }
                return recordList;
            }
        });
        Thread.sleep(1000); // Wait the consumer ready.
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, admin.getBootstrapServers());
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        try (Producer<Integer, String> producer = new KafkaProducer<>(producerProps)) {
            for (int count = 0; count < 10; count++) {
                String value = String.format("Hello %02d", count);
                producer.send(new ProducerRecord<>(topic, count, value));
            }
        }
        List<ConsumerRecord<Integer, String>> recordList = records.get();
        for (ConsumerRecord<Integer, String> record : recordList) {
            assertThat(record.value()).isEqualTo(String.format("Hello %02d", record.key()));
            log.info(record.value());
        }
    }
}
