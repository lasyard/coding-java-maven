package io.github.lasyard.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class JsonViewTest {
    private static final JsonMapper jsonMapper = new JsonMapper();
    private static ObjectMapper yamlMapper = null;

    @BeforeAll
    public static void setupClass() {
        YAMLFactory yamlFactory = new YAMLFactory()
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
        yamlMapper = new ObjectMapper(yamlFactory);
    }

    @Test
    public void testStudent() throws IOException {
        Student student = yamlMapper.readerFor(Student.class).readValue("{ id: 1, name: John, gender: true }");
        String json = jsonMapper.writerWithView(Animal.class).writeValueAsString(student);
        log.info(json);
        assertThat(json).isEqualTo("{\"name\":\"John\",\"gender\":true}");
        json = jsonMapper.writeValueAsString(student);
        log.info(json);
        assertThat(json).isEqualTo("{\"id\":1,\"name\":\"John\",\"gender\":true}");
    }
}
