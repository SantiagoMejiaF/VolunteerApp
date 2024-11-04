package com.constructiveactivists.organizationmodule.controllers.request.activitycoordinator;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityCoordinatorUpdateRequest {

    @NotBlank
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String firstName;

    @NotBlank
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String lastName;

    @NotBlank
    @Schema(description = "Número de celular del coordinador de actividad", example = "3001234567")
    private String phone;
}

