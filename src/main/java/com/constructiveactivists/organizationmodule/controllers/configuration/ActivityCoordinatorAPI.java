package com.constructiveactivists.organizationmodule.controllers.configuration;

import com.constructiveactivists.organizationmodule.controllers.request.activitycoordinator.ActivityCoordinatorRequest;
import com.constructiveactivists.organizationmodule.controllers.request.activitycoordinator.ActivityCoordinatorUpdateRequest;
import com.constructiveactivists.organizationmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Módulo de Organizaciones", description = "Servicios relacionados con la gestión de organizaciones en la aplicación.")
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


    @Operation(summary = "Buscar coordinadores de actividad disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/available")
    ResponseEntity<List<ActivityCoordinatorEntity>> findAvailableCoordinators(
            @RequestParam("organizationId") Integer organizationId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) String startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) String endTime);

    @Operation(summary = "Obtener coordinadores de actividad por organización, sin tener en cuenta la disponibilidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/organization/{organizationId}")
    ResponseEntity<List<ActivityCoordinatorEntity>> getCoordinatorsByOrganization(@PathVariable Integer organizationId);

    @Operation(summary = "Obtener un coordinador de actividad por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<ActivityCoordinatorEntity> getCoordinatorById(@PathVariable Integer id);


    @Operation(summary = "Obtener un coordinador de actividad por su ID de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/user/{userId}")
    ResponseEntity<ActivityCoordinatorEntity> getActivityCoordinatorByUserId(@PathVariable Integer userId);


    @Operation(summary = "Actualizar información del coordinador de actividad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Información actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Coordinador o usuario no encontrado")
    })
    @PutMapping("/{id}")
    ResponseEntity<ActivityCoordinatorEntity> updateActivityCoordinator(
            @PathVariable Integer id,
            @RequestBody @Valid ActivityCoordinatorUpdateRequest request);
}
