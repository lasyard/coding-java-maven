package io.github.lasyard.jmx;

@SuppressWarnings("ALL")
public interface HelloMBean {
    String getName();

    void setName(String name);

    void printHello();

    void printHello(String name);
}
