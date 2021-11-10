package io.github.lasyard.annotation.processor;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ClassArrayProcessor extends ProcessorHelper {
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.<String>builder()
            .add(ClassArray.class.getName())
            .build();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, @Nonnull RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ClassArray.class);
        for (Element element : elements) {
            ClassArray annotation = element.getAnnotation(ClassArray.class);
            ClassArray.Clazz[] classes = annotation.value();
            for (ClassArray.Clazz clazz : classes) {
                processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.NOTE,
                    "Clazz.name is " + clazz.name(),
                    element
                );
            }
            AnnotationMirror annotationMirror = getAnnotationMirror(element, ClassArray.class);
            AnnotationValue annotationValue = getAnnotationValue(
                Objects.requireNonNull(annotationMirror),
                "value"
            );
            @SuppressWarnings("unchecked")
            List<? extends AnnotationValue> values =
                (List<? extends AnnotationValue>) Objects.requireNonNull(annotationValue).getValue();
            for (AnnotationValue value : values) {
                AnnotationMirror mirror = (AnnotationMirror) value.getValue();
                AnnotationValue clazz = getAnnotationValue(mirror, "value");
                processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.NOTE, "Clazz.value is " + Objects.requireNonNull(clazz).getValue(),
                    element,
                    mirror,
                    clazz
                );
            }
        }
        return true;
    }
}
