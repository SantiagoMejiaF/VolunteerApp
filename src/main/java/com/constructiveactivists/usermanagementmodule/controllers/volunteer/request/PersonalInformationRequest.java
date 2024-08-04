package com.constructiveactivists.usermanagementmodule.controllers.volunteer.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
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
    @Max(110)
    @Min(8)
    @Schema(description = "Edad del voluntario", example = "25")
    private Integer age;

}
