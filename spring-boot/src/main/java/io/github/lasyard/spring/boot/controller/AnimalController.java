package io.github.lasyard.spring.boot.controller;

import io.github.lasyard.spring.boot.controller.protocol.Animal;
import io.github.lasyard.spring.boot.controller.protocol.Dog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class AnimalController {
    @GetMapping("dog")
    public Animal getDog() {
        Dog dog = new Dog();
        dog.setName("John");
        return dog;
    }
}
