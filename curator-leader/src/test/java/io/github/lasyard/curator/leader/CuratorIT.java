package io.github.lasyard.curator.leader;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class CuratorIT {
    private static final String TEST_NODE = "/test";

    private static CuratorFramework client = null;

    @BeforeAll
    public static void setupClass() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient("las1:2181", retryPolicy);
        client.start();
        if (client.checkExists().forPath(TEST_NODE) != null) {
            client.delete().forPath(TEST_NODE);
        }
    }

    @AfterAll
    public static void cleanupClass() throws Exception {
        if (client.checkExists().forPath(TEST_NODE) != null) {
            client.delete().forPath(TEST_NODE);
        }
        client.close();
    }

    @Test
    public void testReadWrite() throws Exception {
        client.create().forPath(TEST_NODE, "abc".getBytes(StandardCharsets.UTF_8));
        String r = new String(client.getData().forPath(TEST_NODE), StandardCharsets.UTF_8);
        assertThat(r).isEqualTo("abc");
    }
}
