package com.constructiveactivists.usermanagementmodule.controllers.volunteer.request;

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
public class PersonalInformationRequest {

    @NotBlank
    @Size(min = 1, max = 50)
    @Schema(description = "Nombre del voluntario", example = "Santiago")
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 50)
    @Schema(description = "Apellido del voluntario", example = "Mejía Fernández")
    private String lastName;

    @NotBlank
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^\\d{10}$", message = "La cédula debe contener exactamente 10 dígitos")
    @Schema(description = "Cédula del voluntario", example = "1000286136")
    private String identificationCard;

    @NotBlank
    @Email
    @Size(min = 1, max = 50)
    @Schema(description = "Correo electrónico del voluntario", example = "santiagomejia2000@hotmail.com")
    private String email;

    @NotBlank
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^\\d{10}$", message = "El número de celular debe contener exactamente 10 dígitos")
    @Schema(description = "Número de celular del voluntario", example = "3223045822")
    private String phoneNumber;

    @NotBlank
    @Size(min = 1, max = 50)
    @Schema(description = "Dirección del voluntario", example = "Calle 23 #68-59")
    private String address;
}
