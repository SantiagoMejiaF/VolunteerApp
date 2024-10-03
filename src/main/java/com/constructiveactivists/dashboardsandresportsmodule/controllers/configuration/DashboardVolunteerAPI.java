package com.constructiveactivists.dashboardsandresportsmodule.controllers.configuration;

import com.constructiveactivists.dashboardsandresportsmodule.controllers.response.CardsOrganizationVolunteerResponse;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Tag(name = "Módulo de Tableros de Control y Reportes", description = "Servicios relacionados con los tableros de control que tiene" +
        "el administrador, los voluntarios y las organizaciones junto con sus reportes.")
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

    @Operation(summary = "Obtener las cards de fundaciones a las que pertenece un voluntario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/foundations/{volunteerId}")
    ResponseEntity<List<CardsOrganizationVolunteerResponse>> getFoundationsByVolunteerId(@PathVariable Integer volunteerId);
}
