package io.github.lasyard.flink.cep;

import io.github.lasyard.flink.test.sink.TestSink;
import org.apache.flink.cep.nfa.aftermatch.AfterMatchSkipStrategy;
import org.apache.flink.cep.pattern.MalformedPatternException;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.test.util.MiniClusterWithClientResource;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class CepTest {
    private static final String TEST_STRING = "- a1 b1 b2 - a2 - b3 c1 - a3 - - c2 b4 b5 - b6 - c3 - b7 -";

    @ClassRule
    public static MiniClusterWithClientResource cluster =
        new MiniClusterWithClientResource(new MiniClusterResourceConfiguration.Builder()
            .setNumberSlotsPerTaskManager(2)
            .setNumberTaskManagers(1)
            .build());

    @Before
    public void setup() {
        TestSink.clear();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testNext() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
            .next("2").where(new Condition("b"));
        Matcher.match(TEST_STRING, pattern);
        TestSink.assertValues().containsExactly("a1 b1");
    }

    @Test
    public void testFollowedBy() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
            .followedBy("2").where(new Condition("b"));
        Matcher.match(TEST_STRING, pattern);
        TestSink.assertValues().containsExactly("a1 b1", "a2 b3", "a3 b4");
    }

    @Test
    public void testFollowedBy1() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
            .followedBy("2").where(new Condition("b"))
            .followedBy("3").where(new Condition("a"));
        Matcher.match(TEST_STRING, pattern);
        TestSink.assertValues().containsExactly("a1 b1 a2", "a2 b3 a3");
    }

    @Test
    public void testFollowedByAny() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
            .followedByAny("2").where(new Condition("b"));
        Matcher.match(TEST_STRING, pattern);
        TestSink.assertValues().containsExactly(
            "a1 b1",
            "a1 b2",
            "a1 b3", "a2 b3",
            "a1 b4", "a2 b4", "a3 b4",
            "a1 b5", "a2 b5", "a3 b5",
            "a1 b6", "a2 b6", "a3 b6",
            "a1 b7", "a2 b7", "a3 b7"
        );
    }

    @Test
    public void testNotNext() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
            .notNext("2").where(new Condition("b"));
        Matcher.match(TEST_STRING, pattern);
        TestSink.assertValues().containsExactly("a2", "a3");
    }

    @Test
    public void testNotNext2() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
            .notNext("2").where(new Condition("b"))
            .next("3").where(new Condition("a")).optional();
        Matcher.match(TEST_STRING, pattern);
        // Really Problematic
        TestSink.assertValues().containsExactly("a1", "a2", "a3");
    }

    @Test
    public void testNotFollowedBy1() throws Exception {
        assertThrows(
            "NotFollowedBy is not supported as a last part of a Pattern!",
            MalformedPatternException.class,
            () -> {
                Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
                    .notFollowedBy("2").where(new Condition("b"));
                Matcher.match(TEST_STRING, pattern);
            }
        );
    }

    @Test
    public void testNotFollowedBy2() throws Exception {
        assertThrows(
            "The until condition is only applicable to looping states.",
            MalformedPatternException.class,
            () -> {
                Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
                    .notFollowedBy("2").where(new Condition("b"))
                    .until(new Condition("c"));
                Matcher.match(TEST_STRING, pattern);
            }
        );
    }

    @Test
    public void testNotFollowedBy3() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
            .notFollowedBy("2").where(new Condition("b"))
            .followedBy("c").where(new Condition("c"));
        Matcher.match(TEST_STRING, pattern);
        TestSink.assertValues().containsExactly("a3 c2");
    }

    @Test
    public void testNextOneOrMore() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
            .next("2").where(new Condition("b")).oneOrMore();
        Matcher.match(TEST_STRING, pattern);
        TestSink.assertValues().containsExactly(
            "a1 b1",
            "a1 b1 b2",
            "a1 b1 b2 b3",
            "a1 b1 b2 b3 b4",
            "a1 b1 b2 b3 b4 b5",
            "a1 b1 b2 b3 b4 b5 b6",
            "a1 b1 b2 b3 b4 b5 b6 b7"
        );
    }

    @Test
    public void testNextOneOrMoreConsecutive() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
            .next("2").where(new Condition("b")).oneOrMore().consecutive();
        Matcher.match(TEST_STRING, pattern);
        TestSink.assertValues().containsExactly("a1 b1", "a1 b1 b2");
    }

    @Test
    public void testFollowedByTimes() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
            .followedBy("2").where(new Condition("b")).times(2, 4);
        Matcher.match(TEST_STRING, pattern);
        TestSink.assertValues().containsExactly(
            "a1 b1 b2",
            "a1 b1 b2 b3",
            "a1 b1 b2 b3 b4", "a2 b3 b4",
            "a2 b3 b4 b5", "a3 b4 b5",
            "a2 b3 b4 b5 b6", "a3 b4 b5 b6",
            "a3 b4 b5 b6 b7"
        );
    }

    @Test
    public void testFollowedByTimesGreedy() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
            .followedBy("2").where(new Condition("b")).times(2, 4).greedy();
        Matcher.match(TEST_STRING, pattern);
        TestSink.assertValues().containsExactly(
            "a1 b1 b2",
            "a1 b1 b2 b3",
            "a1 b1 b2 b3 b4", "a2 b3 b4",
            "a2 b3 b4 b5", "a3 b4 b5",
            "a2 b3 b4 b5 b6", "a3 b4 b5 b6",
            "a3 b4 b5 b6 b7"
        );
    }

    @Test
    public void testFollowedByOneOrMoreConsecutive() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
            .followedBy("2").where(new Condition("b")).oneOrMore().consecutive();
        Matcher.match(TEST_STRING, pattern);
        TestSink.assertValues().containsExactly(
            "a1 b1", "a1 b1 b2",
            "a2 b3",
            "a3 b4", "a3 b4 b5"
        );
    }

    @Test
    public void testFollowedByOneOrMoreUntil() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
            .followedBy("2").where(new Condition("b")).oneOrMore()
            .until(new Condition("c"));
        Matcher.match(TEST_STRING, pattern);
        TestSink.assertValues().containsExactly(
            "a1 b1", "a1 b1 b2", "a1 b1 b2 b3",
            "a2 b3",
            "a3 b4", "a3 b4 b5", "a3 b4 b5 b6"
        );
    }

    @Test
    public void testFollowedByOneOrMoreGreedyUntil() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
            .followedBy("2").where(new Condition("b")).oneOrMore().greedy()
            .until(new Condition("c"));
        Matcher.match(TEST_STRING, pattern);
        TestSink.assertValues().containsExactly(
            "a1 b1", "a1 b1 b2", "a1 b1 b2 b3",
            "a2 b3",
            "a3 b4", "a3 b4 b5", "a3 b4 b5 b6"
        );
    }

    @Test
    public void testFollowedByOneOrMoreUntilSkip() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin(
                "1",
                AfterMatchSkipStrategy.skipPastLastEvent()
            ).where(new Condition("a"))
            .followedBy("2").where(new Condition("b")).oneOrMore()
            .until(new Condition("c"));
        Matcher.match(TEST_STRING, pattern);
        TestSink.assertValues().containsExactly("a1 b1", "a2 b3", "a3 b4");
    }

    @Test
    public void testGrouping() throws Exception {
        Pattern<String, ?> pattern = Pattern.<String>begin("1").where(new Condition("a"))
            .followedBy("2").where(new Condition("b")).times(2);
        Pattern<String, ?> pattern1 = Pattern.begin(pattern).times(2);
        Matcher.match(TEST_STRING, pattern1);
        TestSink.assertValues().containsExactly("a1 a2 b1 b2 b3 b4");
    }
}
