package io.github.lasyard.annotation.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ClassArray {
    Clazz[] value();

    @interface Clazz {
        String name();

        Class<?> value();
    }
}
