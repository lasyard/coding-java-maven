package io.github.lasyard.utils;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import javax.annotation.Nonnull;

public final class WebUtils {
    private WebUtils() {
    }

    @Nonnull
    public static byte[] downloadFromUrl(@Nonnull URL url) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedInputStream in = new BufferedInputStream(url.openStream());
        IOUtils.copy(in, out);
        return out.toByteArray();
    }
}
