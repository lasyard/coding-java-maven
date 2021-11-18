package io.github.lasyard.kafka.admin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
public class KafkaAdmin {
    @Getter
    private final String bootstrapServers;

    public AdminClient getAdminClient() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(AdminClientConfig.CLIENT_ID_CONFIG, getClass().getSimpleName());
        return AdminClient.create(props);
    }

    public void createTopic(String topic, int partitions, short replicas) {
        try (AdminClient admin = getAdminClient()) {
            admin.createTopics(Collections.singletonList(new NewTopic(topic, partitions, replicas)));
        }
    }

    public Set<String> listTopics() throws InterruptedException, ExecutionException {
        try (AdminClient admin = getAdminClient()) {
            return admin.listTopics().names().get();
        }
    }

    public void deleteTopics(Collection<String> topics) {
        try (AdminClient admin = getAdminClient()) {
            admin.deleteTopics(topics);
        }
        log.debug("Deleted topics \"{}\"", topics);
    }

    public void deleteTopic(String topic) {
        deleteTopics(Collections.singletonList(topic));
    }
}
