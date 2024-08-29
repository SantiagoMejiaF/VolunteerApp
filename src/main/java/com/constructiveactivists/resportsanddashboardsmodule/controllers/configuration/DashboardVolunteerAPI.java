package com.constructiveactivists.resportsanddashboardsmodule.controllers.configuration;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.activity.ActivityEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.InterestEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.SkillEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Tag(name = "Tablero de Control de Voluntarios", description = "Operaciones relacionadas con el tablero de control de voluntarios en la aplicación.")
public interface DashboardVolunteerAPI {

    @Operation(summary = "Obtener la cantidad de voluntarios en cada rango de edad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/volunteer/age-ranges")
    ResponseEntity<Map<String, Long>> getAgeRanges();

    @Operation(summary = "Obtener la edad promedio de los voluntarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/volunteer/average-age")
    ResponseEntity<Double> getAverageAge();

    @Operation(summary = "Obtener la cantidad de voluntarios activos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/active-count")
    ResponseEntity<Long> getActiveVolunteerCount();

    @Operation(summary = "Contador de habilidades de todos los voluntarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/skill-counts")
    ResponseEntity<Map<SkillEnum, Integer>> getSkillCounts();

    @Operation(summary = "Obtener la cantidad de voluntarios disponibles en cada día de la semana")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/availability-count")
    ResponseEntity<Map<AvailabilityEnum, Long>> getVolunteerAvailabilityCount();

    @Operation(summary = "Obtener la cantidad de voluntarios que tienen cada interés")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/interest-count")
    ResponseEntity<Map<InterestEnum, Long>> getInterestCount();

    @Operation(summary = "Obtener la siguiente actividad para un voluntario, sino encuentra ninguna actividad proxima retorna un error")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "204", description = "No hay próximas actividades disponibles"),
            @ApiResponse(responseCode = "404", description = "Voluntario no encontrado")
    })
    @GetMapping("/next-activity/{volunteerId}")
    ResponseEntity<ActivityEntity> getNextActivityForVolunteer(@PathVariable Integer volunteerId);
}
