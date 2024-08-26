package com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.postulation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostulationRequest {

    @NotNull
    @Schema(description = "Identificador del voluntario que se postula", example = "1")
    private Integer volunteerId;

    @Schema(description = "Identificador de la organización asociada a la postulación (puede ser nulo si es independiente)", example = "2")
    private Integer organizationId;

    @Schema(description = "Identificador de la misión a la que se está postulando (puede ser nulo si es una actividad)", example = "5")
    private Integer missionId;

    @Schema(description = "Identificador de la actividad a la que se está postulando (puede ser nulo si es una misión)", example = "3")
    private Integer activityId;

    @Schema(description = "Comentarios adicionales sobre la postulación", example = "Estoy muy interesado en participar en esta misión.")
    private String comments;
}

