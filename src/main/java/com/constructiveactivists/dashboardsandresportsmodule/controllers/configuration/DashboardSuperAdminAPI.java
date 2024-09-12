package com.constructiveactivists.dashboardsandresportsmodule.controllers.configuration;

import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Tag(name = "Módulo de Tableros de Control y Reportes", description = "Servicios relacionados con los tableros de control que tiene" +
        "el administrador, los voluntarios y las organizaciones junto con sus reportes.")
public interface DashboardSuperAdminAPI {

    @Operation(summary = "Obtener la frecuencia de localidades en las que se realizan actividades")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "204", description = "No se encontraron actividades")
    })
    @GetMapping("/activity-localities")
    ResponseEntity<Map<String, Long>> getActivityLocalitiesWithFrequency();

    @Operation(summary = "Obtener el promedio de horas de todos los voluntarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/average-hours/all")
    ResponseEntity<Double> getAverageHoursAllVolunteers();

    @Operation(summary = "Obtener el promedio de horas mensuales de todos los voluntarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/average-monthly-hours/all")
    ResponseEntity<Double> getAverageMonthlyHoursAllVolunteers();

    @Operation(summary = "Obtener los voluntarios más recientes por semana")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "204", description = "No se encontraron voluntarios")
    })
    @GetMapping("/recent-volunteers/week")
    ResponseEntity<List<PostulationEntity>> getRecentVolunteersByWeek();
}