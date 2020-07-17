package com.alternate.easystack.example;

import com.alternate.easystack.core.Context;
import com.alternate.easystack.core.Handler;

public class TestHandler implements Handler<TestRequest, TestResponse> {

    @Override
    public TestResponse apply(Context context, TestRequest testRequest) {
        TestEntity t = context.<TestEntity>get(testRequest.getKey()).orElse(new TestEntity("test"));
        context.save(testRequest.getKey(), t);
        return new TestResponse(t);
    }
}
