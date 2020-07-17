package com.alternate.easystack.example;

import com.alternate.easystack.core.HandlerManager;
import com.alternate.easystack.services.DbService;
import com.alternate.easystack.services.DynamoDbService;

public class App {

    public static void main(String[] args) {
        DynamoDbService.createTable("test");

        DbService dbService = new DynamoDbService("test");
        HandlerManager handlerManager = new HandlerManager(dbService);

        handlerManager.register("/test", TestHandler.class);

        TestRequest testRequest = new TestRequest("test");
        TestResponse testResponse = handlerManager.invoke("/test", testRequest);

        System.out.println("Response: " + testResponse);
    }
}
