package com.constructiveactivists.resportsanddashboardsmodule.controllers.configuration;

import com.constructiveactivists.postulationmanagementmodule.entities.VolunteerOrganizationEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(name = "Tablero de Control de Organizaciones", description = "Operaciones relacionadas con el tablero de control de organizaciones en la aplicación.")
public interface DashboardOrganizationAPI {
    @Operation(summary = "Obtener el número de organizaciones activas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/active-count")
    ResponseEntity<Long> getActiveOrganizationsCount();

    @Operation(summary = "Obtener el número de misiones completadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/complete-missions-count")
    ResponseEntity<Long>getCompleteMissionsCount();

    @Operation(summary = "Obtener el promedio de horas de una organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/average-hours")
    ResponseEntity<Double> getAverageHoursForOrganization(Integer organizationId);

    @Operation(summary = "Obtener el promedio de horas mensuales de una organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/average-monthly-hours")
    ResponseEntity<Double> getAverageMonthlyHoursByOrganization(Integer organizationId);

    @Operation(summary = "Obtener los voluntarios de una organización en un mes específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/volunteers")
    ResponseEntity<List<VolunteerOrganizationEntity>> getVolunteersByOrganizationAndMonth(Integer organizationId, Integer month, Integer year);
}
