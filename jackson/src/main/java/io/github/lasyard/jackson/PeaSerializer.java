package io.github.lasyard.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class PeaSerializer extends StdSerializer<Pea> implements ContextualSerializer {
    private static final long serialVersionUID = -2374770972725620295L;

    private static final PeaSerializer ID_ONLY = new PeaSerializer(null);

    private final JsonSerializer<Object> defaultSerializer;

    protected PeaSerializer(JsonSerializer<Object> defaultSerializer) {
        super(Pea.class);
        this.defaultSerializer = defaultSerializer;
    }

    @Override
    public void serialize(Pea pea, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (defaultSerializer == null) {
            gen.writeNumber(pea.getId());
        } else {
            defaultSerializer.serialize(pea, gen, provider);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(
        SerializerProvider provider,
        BeanProperty property
    ) {
        if (property != null && Pea.class.isAssignableFrom(property.getType().getRawClass())) {
            return ID_ONLY;
        }
        return this;
    }
}
