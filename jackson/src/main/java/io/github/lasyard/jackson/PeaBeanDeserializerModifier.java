package io.github.lasyard.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PeaBeanDeserializerModifier extends BeanDeserializerModifier {
    @SuppressWarnings("unchecked")
    @Override
    public JsonDeserializer<?> modifyDeserializer(
        DeserializationConfig config,
        @NonNull BeanDescription beanDesc,
        JsonDeserializer<?> deserializer
    ) {
        if (Pea.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new PeaDeserializer((JsonDeserializer<Object>) deserializer);
        }
        return deserializer;
    }
}
