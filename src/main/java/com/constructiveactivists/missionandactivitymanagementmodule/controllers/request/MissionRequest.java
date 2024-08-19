package com.constructiveactivists.missionandactivitymanagementmodule.controllers.request;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.enums.MissionTypeEnum;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.enums.VolunteerMissionRequirements;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.SkillEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MissionRequest {

    @NotNull
    @Schema(description = "Identificador de la organización que creó la misión", example = "1")
    private Integer organizationId;

    @NotBlank
    @Schema(description = "Título de la misión", example = "Campaña de Reforestación")
    private String title;

    @NotNull
    @Schema(description = "Indica si la misión es pública", example = "0")
    private Boolean isPublic;

    @NotBlank
    @Schema(description = "Descripción detallada de la misión", example = "Reforestación de áreas verdes en la comunidad local.")
    private String description;

    @NotNull
    @Schema(description = "Fecha de inicio de la misión", example = "2024-09-01")
    private LocalDate startDate;

    @NotNull
    @Schema(description = "Fecha de finalización de la misión", example = "2024-09-10")
    private LocalDate endDate;

    @NotBlank
    @Schema(description = "Ciudad donde se llevará a cabo la misión", example = "Bogotá")
    private String city;

    @NotNull
    @Schema(description = "Número de voluntarios requeridos", example = "50")
    private Integer numberOfVolunteersRequired;

    @NotNull
    @Schema(description = "Horas de voluntariado requeridas", example = "40")
    private Integer requiredHours;

    @NotNull
    @Schema(description = "Lista de requisitos de la misión", example = "[\"EXPERIENCIA_PREVIA\", \"ACCESO_TRANSPORTE\"]")
    private List<VolunteerMissionRequirements> volunteerMissionRequirementsList;

    @NotNull
    @Schema(description = "Lista de habilidades requeridas para la misión", example = "[\"TRABAJO_EN_EQUIPO\", \"COMUNICACION\", \"LIDERAZGO\"]")
    private List<SkillEnum> requiredSkillsList;

    @NotNull
    @Schema(description = "Tipo de misión", example = "MEDIO_AMBIENTE")
    private MissionTypeEnum missionType;
}
