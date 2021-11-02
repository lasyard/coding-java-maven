package io.github.lasyard.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonSchemaTest {
    @Test
    public void testSchemaGenerator() {
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(
            SchemaVersion.DRAFT_2019_09,
            OptionPreset.PLAIN_JSON
        )
            .without(Option.SCHEMA_VERSION_INDICATOR)
            .with(new JacksonModule(JacksonOption.IGNORE_TYPE_INFO_TRANSFORM));
        configBuilder.forFields().withRequiredCheck(f -> f.getAnnotation(JsonProperty.class).required());
        SchemaGeneratorConfig config = configBuilder.build();
        SchemaGenerator generator = new SchemaGenerator(config);
        JsonNode jsonSchema = generator.generateSchema(Dog.class);
        assertThat(jsonSchema).isInstanceOf(ObjectNode.class);
        assertThat(jsonSchema.get("type").asText()).isEqualTo("object");
        assertThat(jsonSchema.get("required").get(0).asText()).isEqualTo("name");
    }
}
