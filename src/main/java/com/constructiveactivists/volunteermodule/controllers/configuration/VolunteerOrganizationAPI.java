package com.constructiveactivists.volunteermodule.controllers.configuration;

import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.volunteermodule.controllers.request.volunteerorganization.VolunteerOrganizationRequest;
import com.constructiveactivists.volunteermodule.controllers.response.StatusVolunteerOrganizationResponse;
import com.constructiveactivists.volunteermodule.controllers.response.VolunteerOrganizationResponse;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Módulo de Voluntarios", description = "Servicios relacionados con la gestión de voluntarios en la aplicación.")
public interface VolunteerOrganizationAPI {

    @Operation(summary = "Obtener las organizaciones en las que un voluntario ha sido aceptado.")
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
    ResponseEntity<List<VolunteerOrganizationEntity>> getVolunteersByOrganizationId(@PathVariable Integer volunteerOrganizationId);


    @Operation(summary = "Agregar la relacion entre una organización y un voluntario en estado pendiente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @PostMapping("/pending")
    ResponseEntity<VolunteerOrganizationEntity> addVolunteerOrganizationPending(@RequestBody VolunteerOrganizationRequest volunteerOrganizationRequest);

    @Operation(summary = "Obtener detalles general de un voluntario de la relación entre una organización y un voluntario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/{volunteerOrganizationId}")
    ResponseEntity<VolunteerOrganizationResponse> getVolunteerOrganizationDetails(@PathVariable Integer volunteerOrganizationId);

    @Operation(summary = "Obtener voluntarios pendientes por ID de organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/pending/{organizationId}")
    ResponseEntity<List<StatusVolunteerOrganizationResponse>> getPendingVolunteersByOrganizationId(@PathVariable Integer organizationId);

    @Operation(summary = "Obtener voluntarios aceptados por ID de organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/accepted/{organizationId}")
    ResponseEntity<List<StatusVolunteerOrganizationResponse>> getAcceptedVolunteersByOrganizationId(@PathVariable Integer organizationId);

    @Operation(summary = "Obtener voluntarios rechazados por ID de organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/rejected/{organizationId}")
    ResponseEntity<List<StatusVolunteerOrganizationResponse>> getRejectedVolunteersByOrganizationId(@PathVariable Integer organizationId);

    @Operation(summary = "Obtener las últimas cinco organizaciones en las que esta un voluntario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/recent/{volunteerId}")
    ResponseEntity<List<OrganizationEntity>> getRecentFiveOrganizationsByVolunteerId(@PathVariable Integer volunteerId);

    @Operation(summary = "Obtener la lista de los últimos cinco voluntarios aceptados por una organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/recent/list/accepted/{organizationId}")
    ResponseEntity<List<VolunteerEntity>> getRecentAcceptedFiveVolunteersByOrganizationId(
            @PathVariable Integer organizationId);

    @Operation(summary = "Obtener el numero total de los voluntarios aceptados por una organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/count/accepted/{organizationId}")
    ResponseEntity<Long> countAcceptedVolunteers(@PathVariable Integer organizationId);
}
