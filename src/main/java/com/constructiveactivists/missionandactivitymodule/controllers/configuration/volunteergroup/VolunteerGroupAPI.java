package com.constructiveactivists.missionandactivitymodule.controllers.configuration.volunteergroup;

import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Módulo de Misiones y Actividades", description = "Servicios relacionados con la gestión de misiones y actividades en la aplicación.")
public interface VolunteerGroupAPI {

    @Operation(summary = "Obtener un grupo de voluntarios por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Grupo de voluntarios no encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<VolunteerGroupEntity> getVolunteerGroupById(@PathVariable Integer id);

    @Operation(summary = "Obtener todos los grupos de voluntarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping
    ResponseEntity<List<VolunteerGroupEntity>> getAllVolunteerGroups();

    @Operation(summary = "Obtener los grupos de voluntarios por el ID de la organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/organization/{organizationId}")
    ResponseEntity<List<VolunteerGroupEntity>> getVolunteerGroupsByOrganizationId(@PathVariable Integer organizationId);

}
