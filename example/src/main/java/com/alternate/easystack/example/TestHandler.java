package com.alternate.easystack.example;

import com.alternate.easystack.core.Context;
import com.alternate.easystack.core.Handler;

public class TestHandler implements Handler<TestRequest, TestResponse> {

    @Override
    public TestResponse apply(Context context, TestRequest testRequest) {
        TestEntity t = context.<TestEntity>get(TestEntity.class, testRequest.getId()).orElse(new TestEntity(testRequest.getName()));
        context.save(testRequest.getId(), t);
        return new TestResponse(t);
    }
}
