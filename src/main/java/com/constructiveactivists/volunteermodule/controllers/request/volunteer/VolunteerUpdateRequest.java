package com.constructiveactivists.volunteermodule.controllers.request.volunteer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerUpdateRequest {

    @NotNull(message = "La información personal no debe ser nula")
    @Valid
    @Schema(description = "Información personal del voluntario")
    private PersonalUpdateInformationRequest personalUpdateInformationRequest;

    @NotNull(message = "La información del voluntariado no debe ser nula")
    @Valid
    @Schema(description = "Información del voluntariado del voluntario")
    private VolunteeringInformationRequest volunteeringInformation;

    @NotNull(message = "La información de emergencia no debe ser nula")
    @Valid
    @Schema(description = "Información de emergencia del voluntario")
    private EmergencyInformationRequest emergencyInformation;
}
