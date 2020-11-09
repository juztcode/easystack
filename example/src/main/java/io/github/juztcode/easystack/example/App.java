package io.github.juztcode.easystack.example;

import io.github.juztcode.easystack.core.Application;
import io.github.juztcode.easystack.core.DbService;
import io.github.juztcode.easystack.core.RapidoidWebApi;
import io.github.juztcode.easystack.core.WebApi;
import io.github.juztcode.easystack.core.impl.DynamoDbService;
import io.github.juztcode.easystack.example.services.UserService;

public class App {

    public static void main(String[] args) {
        DynamoDbService.createTable("easystack_example");

        DbService dbService = new DynamoDbService("easystack_example");

        Application application = new Application(dbService)
                .registerService(UserService.class);

        WebApi webApi = new RapidoidWebApi("0.0.0.0", 8080);
        webApi.start(application);
    }
}
