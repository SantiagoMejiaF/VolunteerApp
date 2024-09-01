package com.constructiveactivists.usermodule.controllers.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String firstName;
    
    @Schema(description = "Apellido del usuario", example = "Perez")
    private String lastName;

    @Schema(description = "Imagen del usuario", example = "imagen.jpg")
    private String image;
}
