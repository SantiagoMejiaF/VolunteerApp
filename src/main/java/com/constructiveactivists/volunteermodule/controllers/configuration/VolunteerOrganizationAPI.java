package com.constructiveactivists.volunteermodule.controllers.configuration;

import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gestión de Relacion entre Organizaciones y Voluntarios", description = "Servicios relacionadas con la gestión de organizaciones y voluntarios en la aplicación.")
public interface VolunteerOrganizationAPI {

    @Operation(summary = "Obtener organizaciones de voluntarios por ID de voluntario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/volunteer/{volunteerId}")
    ResponseEntity<List<VolunteerOrganizationEntity>> getOrganizationsByVolunteerId(@PathVariable Integer volunteerId);

    @Operation(summary = "Obtener voluntarios por ID de organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/organization/{organizationId}")
    ResponseEntity<List<VolunteerOrganizationEntity>> getVolunteersByOrganizationId(@PathVariable Integer organizationId);
}
