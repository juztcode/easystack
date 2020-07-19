package com.alternate.easystack.example.handlers;

import com.alternate.easystack.core.Context;
import com.alternate.easystack.core.Handler;
import com.alternate.easystack.core.GenericResponse;
import com.alternate.easystack.example.requests.RegisterUserRequest;
import com.alternate.easystack.example.models.User;

import java.util.Optional;

public class RegisterUser implements Handler<RegisterUserRequest, GenericResponse> {

    @Override
    public GenericResponse apply(Context context, RegisterUserRequest request) {
        Optional<User> testEntity = context.get(User.class, request.getUsername());

        if (!testEntity.isPresent()) {
            context.save(request.getUsername(), new User(request.getUsername(), request.getPassword()));
            return new GenericResponse(true, "user registered successfully");
        } else {
            context.logger().error("user already exist for: " + request.getUsername());
            return new GenericResponse(false, "user already exist for: " + request.getUsername());
        }
    }
}
