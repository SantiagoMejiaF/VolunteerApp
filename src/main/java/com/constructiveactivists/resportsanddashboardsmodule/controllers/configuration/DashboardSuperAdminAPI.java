package com.constructiveactivists.resportsanddashboardsmodule.controllers.configuration;

import com.constructiveactivists.postulationmanagementmodule.entities.PostulationEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Tag(name = "Tablero de Control de Super Administrador", description = "Operaciones relacionadas con el tablero de control de super administrador en la aplicación.")
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
    ResponseEntity<List<PostulationEntity>> getRecentVolunteersByWeek();
}
