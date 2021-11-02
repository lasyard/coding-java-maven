package io.github.lasyard.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import javax.annotation.Nonnull;

public class PeaBeanSerializerModifier extends BeanSerializerModifier {
    @SuppressWarnings("unchecked")
    @Override
    public JsonSerializer<?> modifySerializer(
        SerializationConfig config,
        @Nonnull BeanDescription beanDesc,
        JsonSerializer<?> serializer
    ) {
        if (Pea.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new PeaSerializer((JsonSerializer<Object>) serializer);
        }
        return serializer;
    }
}
