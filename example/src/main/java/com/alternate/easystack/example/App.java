package com.alternate.easystack.example;

import com.alternate.easystack.core.DbService;
import com.alternate.easystack.core.DynamoDbService;
import com.alternate.easystack.core.HandlerManager;

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
