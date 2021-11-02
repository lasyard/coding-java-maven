package io.github.lasyard.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class Zoo {
    private final List<Animal> animals;

    @JsonCreator
    public Zoo(@JsonProperty("animals") List<Animal> animals) {
        this.animals = animals;
    }
}
