package io.github.juztcode.easystack.example;

import io.github.juztcode.easystack.core.Application;
import io.github.juztcode.easystack.core.DbService;
import io.github.juztcode.easystack.core.LambdaEndpoint;
import io.github.juztcode.easystack.core.impl.DynamoDbService;
import io.github.juztcode.easystack.example.services.UserService;

public class LambdaApp extends LambdaEndpoint {

    @Override
    public Application getApplication() {
        DbService dbService = new DynamoDbService("easystack_example");

        return new Application(dbService)
                .registerService(UserService.class);
    }
}
