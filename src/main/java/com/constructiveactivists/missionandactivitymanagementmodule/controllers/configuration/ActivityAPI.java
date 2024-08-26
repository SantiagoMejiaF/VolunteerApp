package com.constructiveactivists.missionandactivitymanagementmodule.controllers.configuration;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.activity.ActivityRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.activity.ActivityEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Gestión de Actividades", description = "Operaciones relacionadas con la gestión de actividades en la aplicación.")
public interface ActivityAPI {

    @Operation(summary = "Crear una nueva actividad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Actividad creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    ResponseEntity<ActivityEntity> createActivity(@Valid @RequestBody ActivityRequest activityRequest);


    @Operation(summary = "Obtener una actividad por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada")
    })
    @GetMapping("/{id}")
    ResponseEntity<ActivityEntity> getActivityById(@PathVariable Integer id);


    @Operation(summary = "Obtener todas las actividades")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping
    ResponseEntity<List<ActivityEntity>> getAllActivities();
}
