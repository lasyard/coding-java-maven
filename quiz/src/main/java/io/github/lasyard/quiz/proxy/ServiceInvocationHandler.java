package io.github.lasyard.quiz.proxy;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class ServiceInvocationHandler implements InvocationHandler {
    private final Service impl;

    @Override
    public Object invoke(Object proxy, @NonNull Method method, Object[] args) throws Throwable {
        Object result = method.invoke(impl, args);
        if (result instanceof Long) {
            return (Long) result + 1;
        }
        return result;
    }
}
