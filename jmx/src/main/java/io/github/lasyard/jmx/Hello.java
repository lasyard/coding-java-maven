package io.github.lasyard.jmx;

public class Hello implements HelloMBean {
    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void printHello() {
        printHello(name);
    }

    @Override
    public void printHello(String name) {
        System.out.println("Hello, " + name);
    }
}
