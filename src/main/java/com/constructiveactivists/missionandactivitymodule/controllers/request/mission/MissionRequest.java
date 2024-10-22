package com.constructiveactivists.missionandactivitymodule.controllers.request.mission;

import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionTypeEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VolunteerMissionRequirementsEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
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

    @Schema(description = "Identificador de la organización que creó la misión", example = "1")
    private Integer organizationId;

    @NotNull
    @Schema(description = "Tipo de misión", example = "MEDIO_AMBIENTE")
    private MissionTypeEnum missionType;

    @NotBlank
    @Schema(description = "Título de la misión", example = "Campaña de Reforestación")
    private String title;

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
    @Schema(description = "Departamento donde se llevará a cabo la misión", example = "Cudinamarca")
    private String department;

    @NotNull
    @Schema(description = "Visibilidad de la misión", example = "PUBLICA")
    private VisibilityEnum visibility;

    @NotNull
    @Schema(description = "Lista de requisitos de la misión", example = "[\"EXPERIENCIA_PREVIA\", \"ACCESO_TRANSPORTE\"]")
    private List<VolunteerMissionRequirementsEnum> volunteerMissionRequirementsEnumList;

    @NotNull
    @Schema(description = "Lista de habilidades requeridas para la misión", example = "[\"TRABAJO_EN_EQUIPO\", \"COMUNICACION\", \"LIDERAZGO\"]")
    private List<SkillEnum> requiredSkillsList;

    @NotNull
    @Schema(description = "Lista de intereses requeridos para la misión", example = "[\"MEDIO_AMBIENTE\", \"SALUD\", \"EDUCACION\"]")
    private List<InterestEnum> requiredInterestsList;
}
