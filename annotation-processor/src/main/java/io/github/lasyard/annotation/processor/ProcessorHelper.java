package io.github.lasyard.annotation.processor;

import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.processing.AbstractProcessor;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

abstract class ProcessorHelper extends AbstractProcessor {
    AnnotationMirror getAnnotationMirror(Element element, Class<?> annotationClass) {
        for (AnnotationMirror am : processingEnv.getElementUtils().getAllAnnotationMirrors(element)) {
            if (am.getAnnotationType().toString().equals(annotationClass.getName())) {
                return am;
            }
        }
        return null;
    }

    AnnotationValue getAnnotationValue(@Nonnull AnnotationMirror annotationMirror, String methodName) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry
            : annotationMirror.getElementValues().entrySet()) {
            if (entry.getKey().getSimpleName().toString().equals(methodName)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
