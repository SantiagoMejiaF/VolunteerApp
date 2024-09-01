package com.constructiveactivists.externalservicesmodule.controllers.response;

import com.constructiveactivists.usermodule.entities.UserEntity;

public class AuthenticationResponse {
    private String token;
    private UserEntity user;

    public AuthenticationResponse(String token, UserEntity user) {
        this.token = token;
        this.user = user;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
