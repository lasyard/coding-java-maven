package io.github.lasyard.utils;

import org.apache.commons.io.IOUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public final class WebUtils {
    private WebUtils() {
    }

    public static byte @NonNull [] downloadFromUrl(@NonNull URL url) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedInputStream in = new BufferedInputStream(url.openStream());
        IOUtils.copy(in, out);
        return out.toByteArray();
    }
}
