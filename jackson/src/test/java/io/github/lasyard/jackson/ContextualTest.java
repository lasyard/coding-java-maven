package io.github.lasyard.jackson;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ContextualTest {
    private static final JsonMapper mapper = new JsonMapper();

    @BeforeAll
    public static void setupAll() {
        SimpleModule module = new SimpleModule();
        module.setSerializerModifier(new PeaBeanSerializerModifier());
        module.setDeserializerModifier(new PeaBeanDeserializerModifier());
        mapper.registerModule(module);
    }

    @Test
    public void testSerializer() throws IOException {
        Pea pea = new Pea();
        pea.setId(1);
        pea.setName("main");
        Pea pea1 = new Pea();
        pea1.setId(2);
        pea1.setName("child");
        pea.setPea(pea1);
        Pea pea2 = new Pea();
        pea2.setId(3);
        pea2.setName("grand-child");
        pea1.setPea(pea2);
        String json = mapper.writeValueAsString(pea);
        assertThat(json).isEqualTo("{\"id\":1,\"name\":\"main\",\"pea\":2}");
        json = mapper.writeValueAsString(pea1);
        assertThat(json).isEqualTo("{\"id\":2,\"name\":\"child\",\"pea\":3}");
    }

    @Test
    public void testDeserializer() throws IOException {
        String json = "{\"id\": 1, \"name\": \"main\", \"pea\": 3}";
        Pea pea = mapper.readValue(json, Pea.class);
        assertThat(pea.getId()).isEqualTo(1);
        assertThat(pea.getName()).isEqualTo("main");
        assertThat(pea.getPea().getId()).isEqualTo(3);
        assertThat(pea.getPea().getName()).isNull();
        assertThat(pea.getPea().getPea()).isNull();
    }
}
