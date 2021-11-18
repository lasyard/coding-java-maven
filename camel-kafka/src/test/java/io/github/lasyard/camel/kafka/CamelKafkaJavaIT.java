package io.github.lasyard.camel.kafka;

import io.github.lasyard.kafka.admin.KafkaAdmin;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
public class CamelKafkaJavaIT extends CamelTestSupport {
    private static final KafkaAdmin admin = new KafkaAdmin(CamelKafkaTestUtils.BOOTSTRAP_SERVERS);

    private static String topic = null;

    @BeforeAll
    public static void setupAll() {
        topic = RandomStringUtils.randomAlphabetic(8);
    }

    @AfterAll
    public static void tearDownAll() {
        admin.deleteTopic(topic);
    }

    @Override
    public RouteBuilder createRouteBuilder() {
        return new EndpointRouteBuilder() {
            @Override
            public void configure() {
                from(kafka(topic).brokers(CamelKafkaTestUtils.BOOTSTRAP_SERVERS)).noAutoStartup()
                    .log("Receive message: ${body}.")
                    .to(mock("read-from-kafka"));
            }
        };
    }

    @Test
    public void test() throws Exception {
        CamelKafkaTestUtils.testSend(this, topic);
    }
}
