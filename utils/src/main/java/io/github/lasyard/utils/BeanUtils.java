package io.github.lasyard.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;

public final class BeanUtils {
    private BeanUtils() {
    }

    @Nullable
    public static Object getPropertyValue(@Nonnull Object obj, String propertyName) {
        try {
            Field f = obj.getClass().getField(propertyName);
            if (isPublic(f.getModifiers()) && !isStatic(f.getModifiers())) {
                return f.get(obj);
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        try {
            Method m = obj.getClass().getMethod("get" + StringUtils.capitalize(propertyName));
            if (isPublic(m.getModifiers()) && !isStatic(m.getModifiers())) {
                return m.invoke(obj);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
        }
        return null;
    }
}
