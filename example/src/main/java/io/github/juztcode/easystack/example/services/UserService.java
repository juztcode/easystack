package io.github.juztcode.easystack.example.services;

import io.github.juztcode.easystack.core.DbService;
import io.github.juztcode.easystack.core.Service;
import io.github.juztcode.easystack.example.handlers.RegisterUser;

public class UserService extends Service {
    public UserService(DbService dbService) {
        super(dbService);

        registerHandler(RegisterUser.class);
    }
}
