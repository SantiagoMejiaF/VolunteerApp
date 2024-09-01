package com.constructiveactivists.missionandactivitymodule.controllers.request.volunteergroup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerGroupRequest {

    @Schema(description = "Identificador de la organización", example = "1")
    private Integer organizationId;

    @NotNull
    @Schema(description = "Identificador de la actividad", example = "1")
    private Integer activity;

    @NotBlank
    @Schema(description = "Nombre del grupo de voluntarios", example = "Grupo A de Plantación")
    private String name;

    @Schema(description = "Lista de identificadores de voluntarios en el grupo", example = "[1, 2, 3]")
    private List<VolunteerGroupMembershipRequest> volunteerIds;

    @NotNull
    @Schema(description = "Número de voluntarios requeridos para el grupo", example = "3")
    private Integer numberOfVolunteersRequired;

    @Schema(description = "Observaciones adicionales sobre el grupo de voluntarios", example = "Este grupo se encargará de la plantación en la zona norte del parque.")
    private String observations;
}
