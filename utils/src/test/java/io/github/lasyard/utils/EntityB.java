package io.github.lasyard.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
public class EntityB implements NestedEntity {
    @Getter
    private final String name;

    @Override
    public Collection<? extends NestedEntity> getNested() {
        return null;
    }
}
