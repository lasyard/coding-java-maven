package io.github.lasyard.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class EntityA implements NestedEntity {
    private final List<EntityB> bs;
    @Getter
    public String name = "Aa";

    @Override
    public Collection<? extends NestedEntity> getNested() {
        return bs;
    }
}
