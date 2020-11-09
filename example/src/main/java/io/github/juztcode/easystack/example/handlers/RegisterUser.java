package io.github.juztcode.easystack.example.handlers;

import io.github.juztcode.easystack.core.Context;
import io.github.juztcode.easystack.core.GenericResponse;
import io.github.juztcode.easystack.core.Handler;
import io.github.juztcode.easystack.example.models.User;
import io.github.juztcode.easystack.example.requests.RegisterUserRequest;

import java.util.Optional;

public class RegisterUser implements Handler<RegisterUserRequest, GenericResponse> {

    @Override
    public GenericResponse apply(Context context, RegisterUserRequest request) {
        Optional<User> testEntity = context.get(User.class, request.getUsername());

        if (!testEntity.isPresent()) {
            context.save(request.getUsername(), new User(request.getUsername(), request.getPassword()));
            return GenericResponse.ok("user registered successfully");
        } else {
            context.logger().error("user already exist for: " + request.getUsername());
            return GenericResponse.error("user already exist for: " + request.getUsername());
        }
    }
}
