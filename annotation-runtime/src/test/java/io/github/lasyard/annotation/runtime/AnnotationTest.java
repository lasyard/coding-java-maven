package io.github.lasyard.annotation.runtime;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationTest {
    @Test
    public void test1() {
        Annotation[] annotations = TestObject1.class.getAnnotations();
        List<String> names = Arrays.stream(annotations)
            .map(Annotation::annotationType)
            .map(Class::getSimpleName)
            .collect(Collectors.toList());
        assertThat(names).contains("Annotation1");
    }
}
