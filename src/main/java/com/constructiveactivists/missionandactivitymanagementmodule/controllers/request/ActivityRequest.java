package com.constructiveactivists.missionandactivitymanagementmodule.controllers.request;

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
public class ActivityRequest {

    @NotNull
    @Schema(description = "Identificador de la misión", example = "1")
    private Integer missionId;

    @NotBlank
    @Schema(description = "Título de la actividad", example = "Plantación de árboles")
    private String title;

    @Schema(description = "Descripción de la actividad", example = "Plantación de 100 árboles en el parque central.")
    private String description;

    @NotNull
    @Schema(description = "Fecha de la actividad", example = "2024-09-03")
    private LocalDate date;

    @NotBlank
    @Schema(description = "Hora de inicio de la actividad", example = "08:00")
    private String startTime;

    @NotBlank
    @Schema(description = "Hora de finalización de la actividad", example = "12:00")
    private String endTime;

    @NotBlank
    @Schema(description = "Dirección donde se llevará a cabo la actividad", example = "Parque Central, Calle 123")
    private String address;

    @NotNull
    @Schema(description = "Número de voluntarios requeridos para la actividad", example = "20")
    private Integer numberOfVolunteersRequired;

    @NotNull
    @Schema(description = "Horas de voluntariado requeridas", example = "4")
    private Integer requiredHours;

    @Schema(description = "Observaciones adicionales sobre la actividad", example = "Llevar agua y protector solar.")
    private String observations;
}
