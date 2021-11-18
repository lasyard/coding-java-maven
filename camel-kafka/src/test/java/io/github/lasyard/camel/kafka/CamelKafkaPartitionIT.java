package io.github.lasyard.camel.kafka;

import com.google.common.collect.ImmutableMap;
import io.github.lasyard.kafka.admin.KafkaAdmin;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.kafka;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.mock;

@Slf4j
public class CamelKafkaPartitionIT extends CamelTestSupport {
    private static final KafkaAdmin admin = new KafkaAdmin(CamelKafkaTestUtils.BOOTSTRAP_SERVERS);

    private static String topic = null;

    @BeforeAll
    public static void setupClass() {
        topic = RandomStringUtils.randomAlphabetic(8);
    }

    @AfterAll
    public static void tearDownClass() {
        admin.deleteTopic(topic);
    }

    @Override
    public RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(kafka(topic).brokers(CamelKafkaTestUtils.BOOTSTRAP_SERVERS))
                    .noAutoStartup()
                    .process(exchange -> {
                        for (Map.Entry<String, Object> entry : exchange.getIn().getHeaders().entrySet()) {
                            log.info("{}: {}", entry.getKey(), entry.getValue().toString());
                        }
                    })
                    .to(mock("read-from-kafka"));
            }
        };
    }

    @Test
    public void test() throws Exception {
        CamelKafkaTestUtils.testSend(this, topic, ImmutableMap.of(
            KafkaConstants.PARTITION_KEY, 0,
            KafkaConstants.KEY, "test"
        ));
    }
}
