package com.alternate.easystack.example.services;

import com.alternate.easystack.core.DbService;
import com.alternate.easystack.core.Service;
import com.alternate.easystack.example.handlers.RegisterUser;

public class UserService extends Service {
    public UserService(DbService dbService) {
        super(dbService);

        registerHandler(RegisterUser.class);
    }
}
