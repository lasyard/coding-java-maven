package io.github.lasyard.flink.timer;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString(of = {"id", "type", "amount", "timestamp"})
public class Event {
    @CsvBindByPosition(position = 0)
    private Long id;
    @CsvBindByPosition(position = 1)
    private String type;
    @CsvBindByPosition(position = 2)
    private Integer amount;
    @CsvBindByPosition(position = 3)
    private Timestamp timestamp;
}
