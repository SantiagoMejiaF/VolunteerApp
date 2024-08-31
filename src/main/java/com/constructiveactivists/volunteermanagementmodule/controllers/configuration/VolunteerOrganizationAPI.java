package com.constructiveactivists.volunteermanagementmodule.controllers.configuration;

import com.constructiveactivists.volunteermanagementmodule.controllers.request.VolunteerOrganizationRequest;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.OrganizationStatusEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Gestión de Organizaciones de Voluntarios", description = "Operaciones relacionadas con la gestión de organizaciones de voluntarios en la aplicación.")
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

    @Operation(summary = "Actualizar horas de voluntarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @PutMapping("/{id}")
    ResponseEntity<VolunteerOrganizationEntity> updateHours(@PathVariable Integer id,
                                                            @RequestParam Integer hoursCompleted,
                                                            @RequestParam Integer hoursCertified);

    @Operation(summary = "Obtener voluntarios con estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "204", description = "No se encuentran registros con el estado deseado")
    })
    @GetMapping("/status/{status}")
    ResponseEntity<List<VolunteerOrganizationEntity>> getVolunteersWithStatus(@PathVariable OrganizationStatusEnum status);

    @Operation(summary = "Agregar organización de voluntarios pendiente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @PostMapping("/pending")
    ResponseEntity<VolunteerOrganizationEntity> addVolunteerOrganizationPending(@RequestBody VolunteerOrganizationRequest volunteerOrganizationRequest);


}
