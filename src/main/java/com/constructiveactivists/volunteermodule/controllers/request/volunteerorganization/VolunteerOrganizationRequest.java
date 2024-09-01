package com.constructiveactivists.volunteermodule.controllers.request.volunteerorganization;

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
public class VolunteerOrganizationRequest {

    @NotNull
    @Schema(description = "Identificador del voluntario")
    private Integer volunteerId;

    @NotNull
    @Schema(description = "Identificador de la organización")
    private Integer organizationId;
}
