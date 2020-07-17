package com.alternate.easystack.example;

import com.alternate.easystack.core.Response;


public class TestResponse implements Response {
    private final TestEntity testEntity;

    public TestResponse(TestEntity testEntity) {
        this.testEntity = testEntity;
    }

    public TestEntity getTestEntity() {
        return testEntity;
    }

    @Override
    public String toString() {
        return "TestResponse{" +
                "testEntity=" + testEntity +
                '}';
    }
}
