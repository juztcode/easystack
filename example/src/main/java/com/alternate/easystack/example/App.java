package com.alternate.easystack.example;

import com.alternate.easystack.core.Application;
import com.alternate.easystack.core.DbService;
import com.alternate.easystack.core.impl.DynamoDbService;
import com.alternate.easystack.core.RapidoidWebApi;
import com.alternate.easystack.core.WebApi;
import com.alternate.easystack.example.services.UserService;

public class App {

    public static void main(String[] args) {
        DynamoDbService.createTable("easystack_example");

        DbService dbService = new DynamoDbService("easystack_example");

        Application application = new Application(dbService);
        application.registerService(UserService.class);

        WebApi webApi = new RapidoidWebApi("0.0.0.0", 8080);
        webApi.start(application);
    }
}
