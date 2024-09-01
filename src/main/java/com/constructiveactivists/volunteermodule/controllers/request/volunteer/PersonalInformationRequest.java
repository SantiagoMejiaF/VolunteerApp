package com.constructiveactivists.volunteermodule.controllers.request.volunteer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalInformationRequest {

    @NotBlank
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^\\d{10}$", message = "La cédula debe contener exactamente 10 dígitos")
    @Schema(description = "Cédula del voluntario", example = "1000286136")
    private String identificationCard;

    @NotBlank
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^\\d{10}$", message = "El número de celular debe contener exactamente 10 dígitos")
    @Schema(description = "Número de celular del voluntario", example = "3223045822")
    private String phoneNumber;

    @NotBlank
    @Size(min = 1, max = 50)
    @Schema(description = "Dirección del voluntario", example = "Calle 23 #68-59")
    private String address;

    @NotNull
    @Schema(description = "Fecha de nacimiento del voluntario", example = "1999-12-31")
    private LocalDate bornDate;

    @NotBlank
    @Size(min = 1, max = 1000)
    @Schema(description = "Descripción personal del voluntario", example = "Me gusta ayudar a los demás")
    private String personalDescription;
}
