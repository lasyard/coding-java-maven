package io.github.lasyard.utils;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

@RequiredArgsConstructor
public class BufferedReaderIterator implements Iterable<String> {
    private final BufferedReader reader;

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private String line;

            @Override
            public boolean hasNext() {
                try {
                    line = reader.readLine();
                    return line != null;
                } catch (IOException e) {
                    return false;
                }
            }

            @Override
            public String next() {
                return line;
            }
        };
    }
}
