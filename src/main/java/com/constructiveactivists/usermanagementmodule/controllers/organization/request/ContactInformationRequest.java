package com.constructiveactivists.usermanagementmodule.controllers.organization.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactInformationRequest {

    @NotBlank
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^\\d*$", message = "El número de teléfono debe contener solo números")
    @Schema(description = "Número de teléfono de la organización")
    private String phoneNumber;

    @NotBlank
    @Size(min = 1, max = 50)
    @Schema(description = "Dirección de la organización")
    private String address;

    @NotBlank
    @Email
    @Schema(description = "Correo electrónico de la organización")
    private String email;
}
