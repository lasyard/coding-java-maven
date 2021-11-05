package io.github.lasyard.serdes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class Model {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private int score;
}
