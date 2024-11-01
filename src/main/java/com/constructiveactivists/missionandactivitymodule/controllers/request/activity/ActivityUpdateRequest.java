package com.constructiveactivists.missionandactivitymodule.controllers.request.activity;

import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityUpdateRequest {

    @NotBlank
    @Schema(description = "Título de la actividad", example = "Plantación de árboles")
    private String title;

    @NotBlank
    @Schema(description = "Descripción de la actividad", example = "Plantación de 100 árboles en el parque central.")
    private String description;

    @NotNull
    @Schema(description = "ID del coordinador de la actividad", example = "2")
    private Integer activityCoordinator;

    @NotNull
    @Schema(description = "Fecha en la que se realizará la actividad", example = "2024-09-03")
    private LocalDate date;

    @NotBlank
    @Schema(description = "Hora de inicio de la actividad", example = "08:00")
    private String startTime;

    @NotBlank
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
    @Schema(description = "Número de personas beneficiadas por la actividad", example = "100")
    private Integer numberOfBeneficiaries;

    @Schema(description = "Observaciones adicionales sobre la actividad", example = "Llevar agua y protector solar.")
    private String observations;

    @NotNull
    @Schema(description = "Visibilidad de la actividad", example = "PUBLICA")
    private VisibilityEnum visibility;
}
