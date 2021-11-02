package io.github.lasyard.jackson;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.ToString;

@JsonClassDescription("A dog")
// The type name is the simple name of the class if this is not set.
@JsonTypeName(Dog.NAME)
@ToString(callSuper = true)
class Dog extends Animal {
    public static final String NAME = "dog";
}
