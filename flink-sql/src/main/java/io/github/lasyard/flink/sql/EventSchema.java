package io.github.lasyard.flink.sql;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import javax.annotation.Nonnull;

@Getter
@Setter
@RequiredArgsConstructor
public class EventSchema {
    private final String[] keys;
    private final TypeInformation<?>[] types;

    public Event of(@Nonnull Object... objects) {
        Event e = new Event(objects.length);
        int i = 0;
        for (String key : keys) {
            e.put(key, objects[i++]);
        }
        return e;
    }
}
