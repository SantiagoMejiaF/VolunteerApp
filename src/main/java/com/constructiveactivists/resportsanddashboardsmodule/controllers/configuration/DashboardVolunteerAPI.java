package com.constructiveactivists.resportsanddashboardsmodule.controllers.configuration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

public interface DashboardAPI {

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
}
