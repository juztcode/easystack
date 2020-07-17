package com.alternate.easystack.example;

import com.alternate.easystack.core.Request;

public class TestRequest implements Request {
    private final String id;
    private final String name;

    public TestRequest(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "TestRequest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
