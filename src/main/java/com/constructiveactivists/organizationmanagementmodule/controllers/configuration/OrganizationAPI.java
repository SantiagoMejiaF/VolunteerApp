package com.constructiveactivists.organizationmanagementmodule.controllers.configuration;

import com.constructiveactivists.organizationmanagementmodule.controllers.request.OrganizationRequest;
import com.constructiveactivists.organizationmanagementmodule.entities.OrganizationEntity;
import com.constructiveactivists.organizationmanagementmodule.entities.enums.OrganizationTypeEnum;
import com.constructiveactivists.organizationmanagementmodule.entities.enums.SectorTypeEnum;
import com.constructiveactivists.organizationmanagementmodule.entities.enums.VolunteeringTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gestión de Organizaciones", description = "Operaciones relacionadas con la gestión de organizaciones en la aplicación.")
public interface OrganizationAPI {

    @Operation(summary = "Crear una nueva organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Organización creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "409", description = "Conflicto")
    })
    @PostMapping
    ResponseEntity<OrganizationEntity> createOrganization(@Valid @RequestBody OrganizationRequest organizationRequest);

    @Operation(summary = "Obtener una organización por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<OrganizationEntity> getOrganizationById(@PathVariable Integer id);

    @Operation(summary = "Actualizar una organización existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @PutMapping("/{id}")
    ResponseEntity<OrganizationEntity> updateOrganization(@PathVariable Integer id, @Valid @RequestBody OrganizationRequest organizationRequest);

    @Operation(summary = "Eliminar una organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Organización eliminada exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteOrganization(@PathVariable Integer id);

    @Operation(summary = "Obtener todas las organizaciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido")
    })
    @GetMapping
    ResponseEntity<List<OrganizationEntity>> getAllOrganizations();

    @Operation(summary = "Obtener una lista con todos los sectores organizacionales disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "No se encontraron sectores")
    })
    @GetMapping("/sectors-types")
    ResponseEntity<List<SectorTypeEnum>> getAllSectors();

    @Operation(summary = "Obtener una lista con todos los tipos de organización disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "No se encontraron tipos de organización")
    })
    @GetMapping("/organization-types")
    ResponseEntity<List<OrganizationTypeEnum>> getAllOrganizationTypes();

    @Operation(summary = "Obtener una lista con todos los tipos de voluntariado disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "No se encontraron tipos de voluntariado")
    })
    @GetMapping("/volunteering-types")
    ResponseEntity<List<VolunteeringTypeEnum>> getAllVolunteeringTypes();
}
