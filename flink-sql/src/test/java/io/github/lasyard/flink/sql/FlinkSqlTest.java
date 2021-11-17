package io.github.lasyard.flink.sql;

import io.github.lasyard.flink.common.AlwaysEmitStrategy;
import io.github.lasyard.flink.test.sink.TestSink;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.test.util.MiniClusterWithClientResource;
import org.apache.flink.types.Row;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;

import static org.apache.flink.table.api.Expressions.$;

public class FlinkSqlTest {
    @ClassRule
    public static MiniClusterWithClientResource cluster =
        new MiniClusterWithClientResource(new MiniClusterResourceConfiguration.Builder()
            .setNumberSlotsPerTaskManager(2)
            .setNumberTaskManagers(1)
            .build());

    private StreamExecutionEnvironment env;
    private StreamTableEnvironment tblEnv;

    @Nonnull
    public static Event rowToEvent(@Nonnull Row row) {
        Set<String> fieldNames = row.getFieldNames(true);
        assert fieldNames != null;
        Event event = new Event(fieldNames.size() + 1);
        event.put("KIND", row.getKind());
        for (String fieldName : fieldNames) {
            event.put(fieldName, row.getField(fieldName));
        }
        return event;
    }

    @Nonnull
    public static Row eventToRow(@Nonnull Event event) {
        Row row = Row.withNames();
        for (Map.Entry<String, Object> entry : event.entrySet()) {
            row.setField(entry.getKey(), entry.getValue());
        }
        return row;
    }

    @Before
    public void setup() {
        TestSink.clear();
        env = StreamExecutionEnvironment.getExecutionEnvironment()
            .disableOperatorChaining()
            .setParallelism(1);
        tblEnv = StreamTableEnvironment.create(env);
    }

    @Test
    public void testWhere() throws Exception {
        EventSchema schema = new EventSchema(
            new String[]{
                "id", "key"
            },
            new TypeInformation<?>[]{
                TypeInformation.of(Integer.class),
                TypeInformation.of(String.class)
            }
        );
        DataStream<Event> in = env.fromElements(
            schema.of(1, "a"),
            schema.of(2, "b"),
            schema.of(3, "c"),
            schema.of(4, "d"),
            schema.of(5, "e"),
            schema.of(6, "a"),
            schema.of(7, "c"),
            schema.of(8, "d"),
            schema.of(9, "f"),
            schema.of(10, "a"),
            schema.of(11, "e"),
            schema.of(12, "a")
        );
        Table tbl = tblEnv.fromDataStream(in
            .map(FlinkSqlTest::eventToRow)
            .returns(new RowTypeInfo(schema.getTypes(), schema.getKeys()))
        );
        Table res = tblEnv.sqlQuery("select id, key from " + tbl + " where key = 'a'");
        tblEnv.toDataStream(res)
            .map(FlinkSqlTest::rowToEvent)
            .map(Event::toString)
            .addSink(new TestSink<>())
            .setParallelism(1);
        env.execute();
        TestSink.assertValues().containsExactly(
            "KIND: INSERT, id: 1, key: a",
            "KIND: INSERT, id: 6, key: a",
            "KIND: INSERT, id: 10, key: a",
            "KIND: INSERT, id: 12, key: a"
        );
    }

    @Test
    public void testGroup() throws Exception {
        EventSchema schema = new EventSchema(
            new String[]{
                "id", "key"
            },
            new TypeInformation<?>[]{
                TypeInformation.of(Integer.class),
                TypeInformation.of(String.class)
            }
        );
        DataStream<Event> in = env.fromElements(
            schema.of(1, "a"),
            schema.of(2, "b"),
            schema.of(3, "c"),
            schema.of(4, "d"),
            schema.of(5, "e"),
            schema.of(6, "a"),
            schema.of(7, "c"),
            schema.of(8, "d"),
            schema.of(9, "f"),
            schema.of(10, "a"),
            schema.of(11, "e"),
            schema.of(12, "a")
        );
        Table tbl = tblEnv.fromDataStream(in
            .map(FlinkSqlTest::eventToRow)
            .returns(new RowTypeInfo(schema.getTypes(), schema.getKeys()))
        );
        Table res = tblEnv.sqlQuery("select key, count(key) as cnt from " + tbl + " group by key");
        tblEnv.toChangelogStream(res)
            .map(FlinkSqlTest::rowToEvent)
            .map(Event::toString)
            .addSink(new TestSink<>())
            .setParallelism(1);
        env.execute();
        TestSink.assertValues().containsExactly(
            "KIND: INSERT, cnt: 1, key: a",
            "KIND: INSERT, cnt: 1, key: b",
            "KIND: INSERT, cnt: 1, key: c",
            "KIND: INSERT, cnt: 1, key: d",
            "KIND: INSERT, cnt: 1, key: e",
            "KIND: UPDATE_BEFORE, cnt: 1, key: a",
            "KIND: UPDATE_AFTER, cnt: 2, key: a",
            "KIND: UPDATE_BEFORE, cnt: 1, key: c",
            "KIND: UPDATE_AFTER, cnt: 2, key: c",
            "KIND: UPDATE_BEFORE, cnt: 1, key: d",
            "KIND: UPDATE_AFTER, cnt: 2, key: d",
            "KIND: INSERT, cnt: 1, key: f",
            "KIND: UPDATE_BEFORE, cnt: 2, key: a",
            "KIND: UPDATE_AFTER, cnt: 3, key: a",
            "KIND: UPDATE_BEFORE, cnt: 1, key: e",
            "KIND: UPDATE_AFTER, cnt: 2, key: e",
            "KIND: UPDATE_BEFORE, cnt: 3, key: a",
            "KIND: UPDATE_AFTER, cnt: 4, key: a"
        );
    }

    @Test
    public void testCep() throws Exception {
        EventSchema schema = new EventSchema(
            new String[]{
                "type",
                "name",
                "ts",
            },
            new TypeInformation<?>[]{
                TypeInformation.of(Integer.class),
                TypeInformation.of(String.class),
                TypeInformation.of(Long.class)
            }
        );
        DataStream<Event> in = env.fromElements(
            schema.of(1, "a", 1L),
            schema.of(1, "b", 2L),
            schema.of(1, "b", 3L),
            schema.of(3, "a", 4L),
            schema.of(3, "c", 5L),
            schema.of(3, "b", 6L),
            schema.of(1, "b", 7L),
            schema.of(2, "a", 8L),
            schema.of(3, "a", 9L),
            schema.of(3, "b", 10L),
            schema.of(1, "b", 11L),
            schema.of(1, "a", 12L)
        ).assignTimestampsAndWatermarks(
            new AlwaysEmitStrategy<>(event -> (Long) event.get("ts"))
        );
        Schema.Builder builder = Schema.newBuilder();
        DataStream<Row> rowStream = in.map(FlinkSqlTest::eventToRow);
        // Though deprecated, this is the only way works here.
        Table tbl = tblEnv.fromDataStream(in
                .map(FlinkSqlTest::eventToRow)
                .returns(new RowTypeInfo(schema.getTypes(), schema.getKeys())),
            $("type"),
            $("name"),
            $("ts"),
            $("rowTime").rowtime()
        );
        tbl.printSchema();
        Table res = tblEnv.sqlQuery(
            "SELECT t.aType, t.bType, t.a, t.b, t.aTs, t.bTs\n"
                + "FROM " + tbl + "\n"
                + "MATCH_RECOGNIZE (\n"
                + "  PARTITION BY type\n"
                + "  ORDER BY rowTime\n"
                + "  MEASURES A.type AS aType, B.type AS bType, A.name AS a, B.name AS b, A.ts AS aTs, B.ts AS bTs\n"
                + "  PATTERN (A B)\n"
                + "  DEFINE A AS name = 'a', B AS name = 'b'\n"
                + ") AS t"
        );
        tblEnv.toDataStream(res)
            .map(FlinkSqlTest::rowToEvent)
            .map(Event::toString)
            .addSink(new TestSink<>());
        env.execute();
        TestSink.assertValues().containsExactly(
            "KIND: INSERT, a: a, aTs: 1, aType: 1, b: b, bTs: 2, bType: 1",
            "KIND: INSERT, a: a, aTs: 9, aType: 3, b: b, bTs: 10, bType: 3"
        );
    }
}
