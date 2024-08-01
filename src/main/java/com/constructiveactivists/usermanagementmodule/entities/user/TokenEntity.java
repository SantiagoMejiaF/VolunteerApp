package com.constructiveactivists.usermanagementmodule.entities.user;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
public class TokenEntity {
    @NotNull
    private String value;
}
