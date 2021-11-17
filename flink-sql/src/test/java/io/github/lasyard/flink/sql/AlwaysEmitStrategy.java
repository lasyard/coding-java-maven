package io.github.lasyard.flink.sql;

import lombok.RequiredArgsConstructor;
import org.apache.flink.api.common.eventtime.TimestampAssigner;
import org.apache.flink.api.common.eventtime.TimestampAssignerSupplier;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkGeneratorSupplier;
import org.apache.flink.api.common.eventtime.WatermarkOutput;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;

@RequiredArgsConstructor
public class AlwaysEmitStrategy<T> implements WatermarkStrategy<T> {
    private static final long serialVersionUID = -5541137241479157740L;

    private final TimeExtractor<T> timeExtractor;

    @Override
    public TimestampAssigner<T> createTimestampAssigner(TimestampAssignerSupplier.Context context) {
        return (event, timestamp) -> timeExtractor.get(event);
    }

    @Override
    public WatermarkGenerator<T> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
        return new WatermarkGenerator<T>() {
            @Override
            public void onEvent(T event, long eventTimestamp, WatermarkOutput output) {
                output.emitWatermark(new Watermark(eventTimestamp));
            }

            @Override
            public void onPeriodicEmit(WatermarkOutput output) {
            }
        };
    }
}
