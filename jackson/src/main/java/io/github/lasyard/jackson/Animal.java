package io.github.lasyard.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
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
@ToString
@EqualsAndHashCode
abstract class Animal {
    @JsonProperty(value = "name", required = true)
    @JsonPropertyDescription("The name")
    @Getter
    @Setter
    private String name;
    @JsonProperty("species")
    @Getter
    @Setter
    private AnimalSpecies species;
}
