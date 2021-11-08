package io.github.lasyard.js;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Slf4j
public class JsTypeTest {
    private static Invocable invocable = null;

    @Nonnull
    public static Stream<Arguments> getArguments() {
        return Stream.of(
            arguments("test", "string", "java.lang.String"),
            arguments(1, "number", "java.lang.Integer"),
            arguments(1L, "object", "java.lang.Long"),
            arguments(1.0, "number", "java.lang.Double"),
            arguments(1.0f, "number", "java.lang.Float"),
            arguments(new Object(), "object", "java.lang.Object"),
            arguments(new HashMap<String, Object>(), "object", "java.util.HashMap")
        );
    }

    @BeforeAll
    public static void setupAll() throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new InputStreamReader(Objects.requireNonNull(
            JsTypeTest.class.getResourceAsStream("/type-test.js")
        )));
        invocable = (Invocable) engine;
    }

    @ParameterizedTest
    @MethodSource("getArguments")
    public void test(Object obj, String jsType, String javaType) throws Exception {
        ScriptObjectMirror result = (ScriptObjectMirror) invocable.invokeFunction("test", obj);
        String jsTypeResult = (String) result.getMember("jsType");
        String javaTypeResult = (String) result.getMember("jType");
        log.info("jsType = {}, jType = {}", jsTypeResult, javaTypeResult);
        assertEquals(jsTypeResult, jsType);
        assertEquals(javaTypeResult, javaType);
    }
}
