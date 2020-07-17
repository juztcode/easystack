package com.alternate.easystack.example;

import com.alternate.easystack.core.Context;
import com.alternate.easystack.core.Handler;

import java.util.Optional;

public class CreateUser implements Handler<CreateUserRequest, GenericResponse> {

    @Override
    public GenericResponse apply(Context context, CreateUserRequest request) {
        Optional<User> testEntity = context.get(User.class, request.getId());

        if (!testEntity.isPresent()) {
            context.save(request.getId(), new User(request.getId(), request.getName()));
            return new GenericResponse(true, "user created successfully");
        } else {
            return new GenericResponse(false, "user already exist");
        }
    }
}
