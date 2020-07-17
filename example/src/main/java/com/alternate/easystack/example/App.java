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

        handlerManager.register("/test", TestHandler.class);

        WebApi webApi = new RapidoidWebApi();
        webApi.start(handlerManager);
    }
}
