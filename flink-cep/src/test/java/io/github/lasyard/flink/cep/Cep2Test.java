package io.github.lasyard.flink.cep;

import io.github.lasyard.flink.test.sink.TestSink;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.test.util.MiniClusterWithClientResource;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@Slf4j
public class Cep2Test {
    private static final String TEST_STRING = "a1 a2 a3 a4 c5 b6 b7";

    @ClassRule
    public static MiniClusterWithClientResource cluster =
        new MiniClusterWithClientResource(new MiniClusterResourceConfiguration.Builder()
            .setNumberSlotsPerTaskManager(2)
            .setNumberTaskManagers(1)
            .build());

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        TestSink.clear();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test1() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a")).times(1, 3).greedy()
            .followedBy("2").where(new Condition("b"));
        Matcher.match(TEST_STRING, pattern);
        TestSink.assertValues().containsExactly(
            "a2 a3 a4 b6",
            "a3 a4 b6",
            "a4 b6"
        );
    }
}
