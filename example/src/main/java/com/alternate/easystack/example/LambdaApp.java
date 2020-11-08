package com.alternate.easystack.example;

import com.alternate.easystack.core.Application;
import com.alternate.easystack.core.DbService;
import com.alternate.easystack.core.impl.DynamoDbService;
import com.alternate.easystack.core.LambdaEndpoint;
import com.alternate.easystack.example.services.UserService;

public class LambdaApp extends LambdaEndpoint {

    @Override
    public Application getApplication() {
        DbService dbService = new DynamoDbService("easystack_example");

        return new Application(dbService)
                .registerService(UserService.class);
    }
}
