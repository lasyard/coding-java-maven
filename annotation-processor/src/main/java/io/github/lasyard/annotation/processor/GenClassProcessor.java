package io.github.lasyard.annotation.processor;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
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
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class GenClassProcessor extends ProcessorHelper {
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.<String>builder()
                .add(GenClass.class.getName())
                .build();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, @Nonnull RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(GenClass.class);
        for (Element element : elements) {
            GenClass annotation = element.getAnnotation(GenClass.class);
            Element pkg = element.getEnclosingElement();
            if (pkg.getKind() != ElementKind.PACKAGE) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "Parent is not a package.",
                        element
                );
                continue;
            }
            String packageName = pkg.asType().toString();
            // Cannot get Class<?> value like `annotation.returnType()`.
            AnnotationMirror annotationMirror = getAnnotationMirror(element, GenClass.class);
            AnnotationValue returnTypeValue = getAnnotationValue(
                    Objects.requireNonNull(annotationMirror),
                    "returnType"
            );
            Class<?> returnType = null;
            try {
                returnType = Class.forName(Objects.requireNonNull(returnTypeValue).getValue().toString());
            } catch (ClassNotFoundException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getLocalizedMessage());
            }
            try {
                MethodSpec methodSpec = MethodSpec.methodBuilder(annotation.methodName())
                        .returns(Objects.requireNonNull(returnType))
                        .addCode(annotation.code())
                        .build();
                TypeSpec typeSpec = TypeSpec.classBuilder(annotation.className())
                        .addMethod(methodSpec)
                        .build();
                JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                        .indent("    ")
                        .build();
                javaFile.writeTo(processingEnv.getFiler());
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.NOTE,
                        "Class file \"" + packageName + "." + annotation.className() + "\" generated.",
                        element,
                        annotationMirror
                );
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getLocalizedMessage());
            }
        }
        return true;
    }
}
