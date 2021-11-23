package io.github.lasyard.spring.boot.controller.protocol;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.ToString;

@JsonTypeName("dog")
@ToString(callSuper = true)
public class Dog extends Animal {
}
