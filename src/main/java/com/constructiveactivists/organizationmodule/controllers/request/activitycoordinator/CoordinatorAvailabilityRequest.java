package com.constructiveactivists.organizationmodule.controllers.request.activitycoordinator;

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
public class CoordinatorAvailabilityRequest {
    @NotNull
    @Schema(description = "Identificador de la organización")
    private Integer organizationId;

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
}
