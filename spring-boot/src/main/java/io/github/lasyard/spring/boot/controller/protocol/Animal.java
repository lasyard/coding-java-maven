package io.github.lasyard.spring.boot.controller.protocol;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(Dog.class),
    @JsonSubTypes.Type(Cat.class),
})
@Getter
@Setter
@ToString
@EqualsAndHashCode
public abstract class Animal {
    private String name;
}
