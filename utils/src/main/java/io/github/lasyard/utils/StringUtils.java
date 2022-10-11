package io.github.lasyard.utils;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class StringUtils {
    private StringUtils() {
    }

    public static @NonNull String uppercaseFirst(@NonNull String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
