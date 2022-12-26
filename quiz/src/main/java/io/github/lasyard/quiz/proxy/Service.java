package io.github.lasyard.quiz.proxy;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.Proxy;

public interface Service {
    static @NonNull Service proxy(Service impl) {
        return (Service) Proxy.newProxyInstance(
            Service.class.getClassLoader(),
            new Class[]{Service.class},
            new ServiceInvocationHandler(impl)
        );
    }

    long num();
}
