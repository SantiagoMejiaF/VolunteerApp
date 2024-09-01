package com.constructiveactivists.externalservicesmodule.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
public class TokenModel {

    @NotNull
    private String value;

    public TokenModel(String jwt) {
    }
}
