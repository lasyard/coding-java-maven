package io.github.lasyard.mybatis;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"test"})
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ModelMapperTest {
    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testFindAll() {
        List<Model> modelList = modelMapper.findAll();
        Model model = modelList.get(0);
        assertThat(model.getId()).isEqualTo(1);
        assertThat(model.getName()).isEqualTo("Alice");
    }

    @Test
    public void testFindById() {
        Model model = modelMapper.findById(1);
        assertThat(model.getId()).isEqualTo(1);
        assertThat(model.getName()).isEqualTo("Alice");
    }

    @Test
    public void testInsert() {
        Model model = new Model();
        model.setName("Bob");
        Integer result = modelMapper.insert(model);
        assertThat(result).isEqualTo(1);
        assertThat(model.getId()).isEqualTo(2);
    }

    @Configuration
    @EnableAutoConfiguration
    static class Config {
    }
}
