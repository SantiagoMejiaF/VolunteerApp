package com.constructiveactivists.dashboardsandresportsmodule.controllers.configuration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@Tag(name = "Módulo de Tableros de Control y Reportes", description = "Servicios relacionados con los tableros de control que tiene" +
        " el administrador, el voluntario y la organización en la aplicación.")
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
}
