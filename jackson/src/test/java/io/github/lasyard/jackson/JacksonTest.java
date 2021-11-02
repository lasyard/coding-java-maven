package io.github.lasyard.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class JacksonTest {
    private static final ObjectMapper mapper = JsonMapper.builder()
        .addModule(new AfterburnerModule())
        .build();
    private static final Dog dog = new Dog();
    private static final Cat cat = new Cat();

    @BeforeAll
    public static void setupClass() {
        // don't need this if @JsonSubTypes at base class is used.
        // mapper.registerSubtypes(Dog.class, Cat.class);
        dog.setName("Goofy");
        cat.setName("Tom");
    }

    @Test
    public void testDog() throws Exception {
        Dog dog = mapper.readValue(getClass().getResourceAsStream("/dog.json"), Dog.class);
        log.info(dog.toString());
        assertThat(dog).isEqualTo(JacksonTest.dog);
    }

    @Test
    public void testZoo() throws Exception {
        Zoo zoo = mapper.readValue(getClass().getResourceAsStream("/zoo.json"), Zoo.class);
        log.info(zoo.toString());
        assertThat(zoo.getAnimals()).contains(dog, cat);
    }

    @Test
    public void testAnimals() throws Exception {
        List<Animal> animalList = mapper.readValue(
            getClass().getResourceAsStream("/animals.json"),
            new TypeReference<List<Animal>>() {
            }
        );
        log.info(animalList.toString());
        assertThat(animalList).contains(dog, cat);
    }
}
