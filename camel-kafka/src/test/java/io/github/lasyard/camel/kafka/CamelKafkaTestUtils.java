package io.github.lasyard.camel.kafka;

import com.google.common.collect.ImmutableMap;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.kafka;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.mock;

final class CamelKafkaTestUtils {
    static final String BOOTSTRAP_SERVERS
        = ResourceBundle.getBundle("kafka").getString("bootstrap.servers");

    private CamelKafkaTestUtils() {
    }

    static void testSend(
        @Nonnull CamelTestSupport cts,
        String topic
    ) throws Exception {
        testSend(cts, topic, ImmutableMap.of());
    }

    static void testSend(
        @Nonnull CamelTestSupport cts,
        String topic,
        Map<String, Object> headers
    ) throws Exception {
        cts.context().start();
        cts.context().getRouteController().startAllRoutes();
        Thread.sleep(3000); // Wait receiver ready.
        String testString = RandomStringUtils.randomAlphabetic(16);
        String kafkaUri = kafka(topic).brokers(BOOTSTRAP_SERVERS).getUri();
        cts.template().sendBodyAndHeaders(kafkaUri, testString, headers);
        String mockUri = mock("read-from-kafka").getUri();
        MockEndpoint mock = cts.context().getEndpoint(mockUri, MockEndpoint.class);
        mock.expectedMessageCount(1);
        mock.message(0).body().isEqualTo(testString);
        mock.assertIsSatisfied();
        cts.context().stop();
    }
}
