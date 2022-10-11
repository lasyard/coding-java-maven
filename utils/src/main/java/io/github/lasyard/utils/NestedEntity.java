package io.github.lasyard.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface NestedEntity {
    Collection<? extends NestedEntity> getNested();

    default <T extends NestedEntity> Set<String> getPropertySet(@NonNull Class<T> type, String propertyName) {
        Set<String> props = new HashSet<>();
        if (type.isAssignableFrom(this.getClass())) {
            try {
                String value = BeanUtils.getProperty(this, propertyName);
                if (value != null) {
                    props.add(value);
                }
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
            }
        }
        Collection<? extends NestedEntity> children = getNested();
        if (children != null) {
            for (NestedEntity child : children) {
                props.addAll(child.getPropertySet(type, propertyName));
            }
        }
        return props;
    }
}
