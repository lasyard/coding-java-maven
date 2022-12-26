package io.github.lasyard.quiz.proxy;

import java.io.Serializable;

public class ServiceImpl implements Service, Serializable {
    private static final long serialVersionUID = -3384951500348788317L;
    public static final ServiceImpl INSTANCE = new ServiceImpl();

    private ServiceImpl() {
    }

    @Override
    public long num() {
        return serialVersionUID;
    }
}
