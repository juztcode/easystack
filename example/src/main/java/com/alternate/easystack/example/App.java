package com.alternate.easystack.example;

import com.alternate.easystack.core.DbService;
import com.alternate.easystack.core.DynamoDbService;
import com.alternate.easystack.core.HandlerManager;
import com.alternate.easystack.core.RapidoidWebApi;
import com.alternate.easystack.core.WebApi;

public class App {

    public static void main(String[] args) {
        DynamoDbService.createTable("test");

        DbService dbService = new DynamoDbService("test");
        HandlerManager handlerManager = new HandlerManager(dbService);

        handlerManager.register("/create-user", CreateUser.class);

        WebApi webApi = new RapidoidWebApi("0.0.0.0", 8080);
        webApi.start(handlerManager);
    }
}
