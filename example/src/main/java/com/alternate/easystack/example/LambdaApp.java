package com.alternate.easystack.example;

import com.alternate.easystack.core.Application;
import com.alternate.easystack.core.DbService;
import com.alternate.easystack.core.impl.DynamoDbService;
import com.alternate.easystack.core.LambdaEndpoint;
import com.alternate.easystack.example.services.UserService;

public class LambdaApp extends LambdaEndpoint {

    private static final Application application;

    static {
        DynamoDbService.createTable("easystack_example");

        DbService dbService = new DynamoDbService("easystack_example");

        application = new Application(dbService);
        application.registerService(UserService.class);

    }

    @Override
    public Application getApplication() {
        return application;
    }
}
