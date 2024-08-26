package com.constructiveactivists.missionandactivitymanagementmodule.controllers.configuration;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.volunteergroup.VolunteerGroupRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.volunteergroup.VolunteerGroupEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Gestión de Grupos de Voluntarios", description = "Operaciones relacionadas con la gestión de grupos de voluntarios en la aplicación.")
public interface VolunteerGroupAPI {

    @Operation(summary = "Crear un nuevo grupo de voluntarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Grupo de voluntarios creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    ResponseEntity<VolunteerGroupEntity> createVolunteerGroup(@Valid @RequestBody VolunteerGroupRequest volunteerGroupRequest);

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
}
