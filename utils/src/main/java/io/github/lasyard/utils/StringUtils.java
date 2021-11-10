package io.github.lasyard.utils;

import javax.annotation.Nonnull;

public final class StringUtils {
    private StringUtils() {
    }

    @Nonnull
    public static String uppercaseFirst(@Nonnull String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
