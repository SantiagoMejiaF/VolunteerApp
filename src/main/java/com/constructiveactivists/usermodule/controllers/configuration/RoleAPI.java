package com.constructiveactivists.usermodule.controllers.configuration;

import com.constructiveactivists.usermodule.entities.RoleEntity;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Módulo de Usuarios", description = "Servicios relacionados con la gestión de usuarios en la aplicación.")
public interface RoleAPI {

    @Operation(summary = "Ver una lista de roles disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping
    List<RoleEntity> getAllRoles();

    @Operation(summary = "Obtener un rol por Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<RoleEntity> getRoleById(@PathVariable Integer id);

    @Operation(summary = "Obtener un rol por tipo de rol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/type/{roleType}")
    ResponseEntity<RoleEntity> getRoleByType(@PathVariable RoleType roleType);

    @Operation(summary = "Agregar un rol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol creado"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido")
    })
    @PostMapping
    ResponseEntity<RoleEntity> createRole(@RequestBody RoleEntity roleEntity);

    @Operation(summary = "Actualizar un rol por tipo de rol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol actualizado"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @PutMapping("/type/{roleType}")
    ResponseEntity<RoleEntity> updateRoleByType(@PathVariable RoleType roleType, @RequestBody RoleEntity updatedRoleEntity);
}
