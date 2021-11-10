package io.github.lasyard.annotation.process;

import io.github.lasyard.annotation.processor.ClassArray;
import io.github.lasyard.annotation.processor.GenClass;

@GenClass(
    className = "NewClass",
    methodName = "say",
    returnType = String.class,
    code = "return getClass().getSimpleName();\n"
)
@ClassArray({
    @ClassArray.Clazz(name = "Integer", value = Integer.class),
    @ClassArray.Clazz(name = "String", value = String.class),
})
public class GenSources {
}
