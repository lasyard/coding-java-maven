package io.github.lasyard.camel.kafka;

import io.github.lasyard.kafka.admin.KafkaAdmin;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.test.spring.junit5.CamelSpringTestSupport;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Slf4j
public class CamelKafkaXmlIT extends CamelSpringTestSupport {
    private static final KafkaAdmin admin = new KafkaAdmin(CamelKafkaTestUtils.BOOTSTRAP_SERVERS);

    @AfterAll
    public static void tearDownAll() {
        admin.deleteTopic("test-camel-kafka");
    }

    @Override
    public AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("test-camel-kafka.xml");
    }

    @Test
    public void test() throws Exception {
        CamelKafkaTestUtils.testSend(this, "test-camel-kafka");
    }
}
