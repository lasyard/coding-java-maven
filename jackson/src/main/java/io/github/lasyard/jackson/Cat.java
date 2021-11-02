package io.github.lasyard.jackson;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.ToString;

@JsonTypeName(Cat.NAME)
@ToString(callSuper = true)
class Cat extends Animal {
    public static final String NAME = "cat";
}
