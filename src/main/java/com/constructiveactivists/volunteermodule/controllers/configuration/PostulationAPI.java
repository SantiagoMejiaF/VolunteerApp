package com.constructiveactivists.volunteermodule.controllers.configuration;

import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Módulo de Voluntarios", description = "Servicios relacionados con la gestión de voluntarios en la aplicación.")
public interface PostulationAPI {
    @Operation(summary = "Obtener postulaciones pendientes por ID de organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/pending/{organizationId}")
    ResponseEntity<List<PostulationEntity>> getPendingPostulationsByOrganizationId(@PathVariable Integer organizationId);
}
