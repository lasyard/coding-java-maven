package io.github.lasyard.opencsv;

import com.opencsv.bean.CsvToBeanBuilder;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class CsvTest {
    @Test
    public void test() {
        List<Event> events = new CsvToBeanBuilder<Event>(new InputStreamReader(
            Objects.requireNonNull(getClass().getResourceAsStream(("/data.csv"))),
            StandardCharsets.UTF_8)
        )
            .withType(Event.class)
            .build()
            .parse();
        assertThat(events.size()).isEqualTo(9);
        long count = 1;
        for (Event event : events) {
            assertThat(event.getId()).isEqualTo(count++);
        }
    }
}
