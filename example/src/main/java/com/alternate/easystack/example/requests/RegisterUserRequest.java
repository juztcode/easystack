package com.alternate.easystack.example.requests;

import com.alternate.easystack.core.Request;

public class RegisterUserRequest implements Request {
    private final String username;
    private final String password;

    public RegisterUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "RegisterUserRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
