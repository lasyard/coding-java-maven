package io.github.lasyard.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

@JsonPropertyOrder({"id", "name", "gender"})
public class Student {
    @JsonView(Student.class)
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("gender")
    private boolean gender;
}
