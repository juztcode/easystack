package com.alternate.easystack.example;

import com.alternate.easystack.core.Request;

public class CreateUserRequest implements Request {
    private final String id;
    private final String name;

    public CreateUserRequest(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
