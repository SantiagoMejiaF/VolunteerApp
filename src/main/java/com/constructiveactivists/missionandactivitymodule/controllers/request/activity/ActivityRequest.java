package com.constructiveactivists.missionandactivitymodule.controllers.request.activity;

import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRequest {

    @NotNull
    @Schema(description = "Identificador de la misión", example = "1")
    private Long missionId;

    @NotNull
    @Schema(description = "Informacion del lider de comunidad")
    private PersonalDataCommunityLeaderRequest personalDataCommunityLeaderEntity;

    @NotNull
    @Schema(description = "Informacion del coordinador de actividad")
    private Integer activityCoordinator;

    @NotBlank
    @Schema(description = "Título de la actividad", example = "Plantación de árboles")
    private String title;

    @Schema(description = "Descripción de la actividad", example = "Plantación de 100 árboles en el parque central.")
    private String description;

    @NotNull
    @Schema(description = "Fecha de la actividad", example = "2024-09-03")
    private LocalDate date;

    @NotBlank
    @Pattern(
            regexp = "^([01]\\d|2[0-3]):(00|30)$",
            message = "La hora debe estar entre 00:00 y 23:30 y ser un múltiplo de 30 minutos (por ejemplo, 08:00, 08:30)"
    )
    @Schema(description = "Hora de inicio de la actividad", example = "08:00")
    private String startTime;

    @NotBlank
    @Pattern(
            regexp = "^([01]\\d|2[0-3]):(00|30)$",
            message = "La hora debe estar entre 00:00 y 23:30 y ser un múltiplo de 30 minutos (por ejemplo, 08:00, 08:30)"
    )
    @Schema(description = "Hora de finalización de la actividad", example = "12:00")
    private String endTime;

    @NotBlank
    @Schema(description = "Ciudad donde se llevará a cabo la actividad", example = "Bogotá")
    private String city;

    @NotBlank
    @Schema(description = "Localidad donde se llevará a cabo la actividad", example = "Usaquén")
    private String locality;

    @NotBlank
    @Schema(description = "Dirección donde se llevará a cabo la actividad", example = "Parque Central, Calle 123")
    private String address;

    @NotNull
    @Schema(description = "Número de voluntarios requeridos para la actividad", example = "20")
    private Integer numberOfVolunteersRequired;

    @NotNull
    @Schema(description = "Horas de voluntariado requeridas", example = "4")
    private Integer requiredHours;

    @NotNull
    @Schema(description = "Visibilidad de la actividad", example = "PUBLICA")
    private VisibilityEnum visibility;

    @NotNull
    @Schema(description = "Número de personas beneficiadas por la actividad", example = "100")
    private Integer numberOfBeneficiaries;

    @Schema(description = "Observaciones adicionales sobre la actividad", example = "Llevar agua y protector solar.")
    private String observations;
}
