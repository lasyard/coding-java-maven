package io.github.lasyard.kafka.io;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class KafkaAdminIT {
    public static final String BOOTSTRAP_SERVERS = "las1:9092";

    @Test
    public void test() throws Exception {
        String topic = RandomStringUtils.randomAlphabetic(8);
        KafkaAdmin admin = new KafkaAdmin(BOOTSTRAP_SERVERS);
        admin.createTopic(topic, 3, (short) 1);
        Set<String> topics = admin.listTopics();
        assertThat(topics).contains(topic);
        admin.deleteTopic(topic);
    }
}
