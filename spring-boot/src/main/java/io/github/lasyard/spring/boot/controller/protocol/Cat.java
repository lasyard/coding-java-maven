package io.github.lasyard.spring.boot.controller.protocol;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.ToString;

@JsonTypeName("cat")
@ToString(callSuper = true)
public class Cat extends Animal {
}
