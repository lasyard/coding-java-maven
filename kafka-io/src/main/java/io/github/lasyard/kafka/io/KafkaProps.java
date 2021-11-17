package io.github.lasyard.kafka.io;

import java.util.ResourceBundle;

public final class KafkaProps {
    private static final ResourceBundle CONF = ResourceBundle.getBundle("kafka");

    public static final String BOOTSTRAP_SERVERS = CONF.getString("bootstrap.servers");

    private KafkaProps() {
    }
}
