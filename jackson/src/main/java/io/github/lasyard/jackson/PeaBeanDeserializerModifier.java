package io.github.lasyard.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;

import javax.annotation.Nonnull;

public class PeaBeanDeserializerModifier extends BeanDeserializerModifier {
    @SuppressWarnings("unchecked")
    @Override
    public JsonDeserializer<?> modifyDeserializer(
        DeserializationConfig config,
        @Nonnull BeanDescription beanDesc,
        JsonDeserializer<?> deserializer
    ) {
        if (Pea.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new PeaDeserializer((JsonDeserializer<Object>) deserializer);
        }
        return deserializer;
    }
}
