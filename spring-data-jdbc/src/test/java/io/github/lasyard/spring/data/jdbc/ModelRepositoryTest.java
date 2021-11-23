package io.github.lasyard.spring.data.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"test"})
@SpringBootTest
@AutoConfigureJdbc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ModelRepositoryTest {
    @Autowired
    private ModelRepository modelRepository;

    @Test
    public void test() {
        Model model = new Model("haha");
        model = modelRepository.save(model);
        assertThat(model.getId()).isEqualTo(2);
        Iterable<Model> models = modelRepository.findAll();
        List<Model> modelList = new LinkedList<>();
        models.forEach(modelList::add);
        assertThat(modelList).contains(model);
    }

    // Mock application
    @Configuration
    @EnableAutoConfiguration
    static class Config {
    }
}
