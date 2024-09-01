package com.constructiveactivists.volunteermodule.controllers.request.volunteer;

import com.constructiveactivists.volunteermodule.entities.volunteer.enums.RelationshipEnum;
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
public class EmergencyInformationRequest {

    @NotBlank
    @Size(min = 1, max = 50)
    @Schema(description = "Nombre del contacto de emergencia", example = "Adriana")
    private String emergencyContactFirstName;

    @NotBlank
    @Size(min = 1, max = 50)
    @Schema(description = "Apellido del contacto de emergencia", example = "Marcela")
    private String emergencyContactLastName;

    @NotBlank
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^\\d{10}$", message = "El teléfono debe contener exactamente 10 dígitos")
    @Schema(description = "Teléfono del contacto de emergencia", example = "3112494942")
    private String emergencyContactPhone;

    @NotBlank
    @Size(min = 1, max = 50)
    @Email
    @Schema(description = "Correo del contacto de emergencia", example = "daniel@gmail.com")
    private String emergencyContactEmail;

    @NotNull
    @Schema(description = "Parentesco del contacto de emergencia con el voluntario", example = "MADRE")
    private RelationshipEnum emergencyContactRelationship;
}
