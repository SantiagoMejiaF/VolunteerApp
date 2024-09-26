package com.constructiveactivists.missionandactivitymodule.controllers.configuration.activity;

import com.constructiveactivists.missionandactivitymodule.controllers.request.activity.ActivityRequest;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Módulo de Misiones y Actividades", description = "Servicios relacionados con la gestión de misiones y actividades en la aplicación.")
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

    @Operation(summary = "Obtener el código QR para realizar check-in en una actividad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/checkin/{activityId}")
    ResponseEntity<byte[]> getCheckInQrCode(@PathVariable Integer activityId);

    @Operation(summary = "Obtener el código QR para realizar check-out en una actividad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/checkout/{activityId}")
    ResponseEntity<byte[]> getCheckOutQrCode(@PathVariable Integer activityId);

    @Operation(summary = "Obtener todas las actividades de una misión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/mission/{missionId}")
    ResponseEntity<List<ActivityEntity>> getActivitiesByMissionId(@PathVariable Integer missionId);

    @Operation(summary = "Eliminar una actividad por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Actividad eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteActivity(@PathVariable Integer id);
}
