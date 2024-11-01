package com.constructiveactivists.missionandactivitymodule.controllers.configuration.activity;

import com.constructiveactivists.missionandactivitymodule.controllers.request.activity.ActivityRequest;
import com.constructiveactivists.missionandactivitymodule.controllers.request.activity.ActivityUpdateRequest;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

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


    @Operation(summary = "Obtener todas las actividades de un voluntario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/volunteer/{volunteerId}")
    ResponseEntity<List<ActivityEntity>> getVolunteerActivities(@PathVariable Integer volunteerId);


    @Operation(summary = "Obtener todas las actividades de un coordinador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/coordinator/{coordinatorId}")
    ResponseEntity<List<ActivityEntity>> getActivitiesByCoordinator(@PathVariable Integer coordinatorId);


    @Operation(summary = "Obtener todas las actividades programadas (disponibles) de un coordinador")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa",
                    content = @Content (schema = @Schema(implementation = ActivityEntity.class))),
    })
    @GetMapping("/coordinator/{coordinatorId}/available")
    ResponseEntity<List<ActivityEntity>> getAvailableActivitiesByCoordinator(@PathVariable Integer coordinatorId);


    @Operation(summary = "Obtener todas las actividades completadas de un voluntario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/volunteer/{volunteerId}/completed")
    ResponseEntity<Integer> getCompletedActivitiesCountVolunteer(@PathVariable Integer volunteerId);


    @Operation(summary = "Obtener el total de beneficiarios impactados por un voluntario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/volunteer/{volunteerId}/beneficiaries")
    ResponseEntity<Integer> getTotalBeneficiariesImpacted(@PathVariable Integer volunteerId);


    @Operation(summary = "Obtener todas las actividades de un voluntario en un mes y año específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/volunteer/{volunteerId}/activities")
    ResponseEntity<List<ActivityEntity>> getActivitiesByVolunteerAndDate(
            @PathVariable Integer volunteerId,
            @RequestParam int month,
            @RequestParam int year);


    @Operation(summary = "Obtener el promedio de calificación de un voluntario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/volunteer/{volunteerId}/rating")
    ResponseEntity<Double> getAverageRating(@PathVariable Integer volunteerId);


    @Operation(summary = "Obtener el total de actividades realizadas de un voluntario por año")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/volunteer/{volunteerId}/year/{year}")
    ResponseEntity<Map<String, Long>> getActivitiesCountByVolunteerAndYear(
            @PathVariable Integer volunteerId,
            @PathVariable int year);


    @Operation(summary = "Actualizar una actividad existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actividad actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada")
    })
    @PutMapping("/{id}")
    ResponseEntity<ActivityEntity> updateActivity(@PathVariable Integer id, @Valid @RequestBody ActivityUpdateRequest activityUpdateRequest);
}
