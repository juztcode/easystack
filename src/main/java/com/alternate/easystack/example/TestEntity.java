package com.alternate.easystack.example;

public class TestEntity {

    private final String name;

    public TestEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "TestEntity{" +
                "name='" + name + '\'' +
                '}';
    }
}
