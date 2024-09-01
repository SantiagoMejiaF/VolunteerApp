package com.constructiveactivists.volunteermodule.controllers.request.volunteer;

import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
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
public class VolunteerRequest {

    @NotNull
    @Valid
    @Schema(description = "Identificador del usuario al que pertenece el voluntario")
    private Integer userId;

    @NotNull
    @Valid
    @Schema(description = "Visibilidad del perfil del voluntario")
    private VisibilityEnum visibility;

    @NotNull
    @Valid
    @Schema(description = "Información personal del voluntario")
    private PersonalInformationRequest personalInformation;

    @NotNull
    @Valid
    @Schema(description = "Información del voluntariado del voluntario")
    private VolunteeringInformationRequest volunteeringInformation;

    @NotNull
    @Valid
    @Schema(description = "Información de emergencia del voluntario")
    private EmergencyInformationRequest emergencyInformation;
}
