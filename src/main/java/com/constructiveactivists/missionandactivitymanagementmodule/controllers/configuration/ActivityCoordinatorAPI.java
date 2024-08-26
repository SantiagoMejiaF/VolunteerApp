package com.constructiveactivists.missionandactivitymanagementmodule.controllers.configuration;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.activity.ActivityCoordinatorRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gestión de Coordinadores de Actividad", description = "Operaciones relacionadas con la gestión de coordinadores de actividades en la aplicación.")
public interface ActivityCoordinatorAPI {

    @Operation(summary = "Obtener todos los coordinadores de actividad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping
    ResponseEntity<List<ActivityCoordinatorEntity>> getAllActivityCoordinators();

    @Operation(summary = "Crear un nuevo coordinador de actividad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Coordinador creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    ResponseEntity<ActivityCoordinatorEntity> createActivityCoordinator(@RequestBody ActivityCoordinatorRequest request);

    @Operation(summary = "Eliminar un coordinador de actividad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Coordinador eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteActivityCoordinator(@PathVariable Integer id);
}

