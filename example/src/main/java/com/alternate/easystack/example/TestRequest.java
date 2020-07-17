package com.alternate.easystack.example;

import com.alternate.easystack.core.Request;

public class TestRequest implements Request {
    private final String key;

    public TestRequest(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "TestRequest{" +
                "key='" + key + '\'' +
                '}';
    }
}
