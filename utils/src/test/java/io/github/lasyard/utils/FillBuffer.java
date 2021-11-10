package io.github.lasyard.utils;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.nio.charset.StandardCharsets;
import javax.annotation.Nonnull;

class FillBuffer implements Answer<Integer> {
    private final byte[] data;

    // EOF
    FillBuffer() {
        data = null;
    }

    FillBuffer(@Nonnull String data) {
        this.data = data.getBytes(StandardCharsets.US_ASCII);
    }

    @Override
    public Integer answer(InvocationOnMock args) {
        if (data == null) {
            return -1;
        }
        byte[] buf = args.getArgument(0);
        int offset = args.getArgument(1);
        int length = args.getArgument(2);
        length = Math.min(length, data.length);
        System.arraycopy(data, 0, buf, offset, length);
        return length;
    }
}
