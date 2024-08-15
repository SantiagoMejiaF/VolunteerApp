package com.constructiveactivists.usermanagementmodule.controllers.configuration;

import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermanagementmodule.entities.enums.RoleType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gestión de Usuarios", description = "Operaciones relacionadas con la gestión de usuarios en la aplicación.")
public interface UserAPI {

    @Operation(summary = "Ver una lista de usuarios disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping
    List<UserEntity> getAllUsers();

    @Operation(summary = "Obtener un usuario por Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<UserEntity> getUserById(@PathVariable Integer id);

    @Operation(summary = "Actualizar el rol de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @PutMapping("/{id}")
    ResponseEntity<Void> updateUserRoleType(@PathVariable Integer id, @RequestParam RoleType roleType);

    @Operation(summary = "Actualizar el estado de autorización de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @PutMapping("/{userId}")
    ResponseEntity<Void> updateAuthorizationStatus(@PathVariable Integer userId, @RequestParam AuthorizationStatus authorizationStatus);

    @Operation(summary = "Enviar correo de aprobación o rechazo de acceso de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo enviado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping("/{userId}/send-approval-or-rejection-email")
    ResponseEntity<String> sendApprovalOrRejectionEmail(@PathVariable Integer userId, @RequestParam boolean approved);

    @Operation(summary = "Obtener la cantidad total de usuarios registrados en la aplicación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/count")
    ResponseEntity<Long> getTotalUserCount();

    @Operation(summary = "Obtener usuarios por estado de autorización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/status/{authorizationStatus}")
    ResponseEntity<List<UserEntity>> getUsersByAuthorizationStatus(@PathVariable AuthorizationStatus authorizationStatus);

    @Operation(summary = "Obtener voluntarios y organizaciones pendientes de aprobación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/pending")
    ResponseEntity<List<UserEntity>> getPendingVolunteersAndOrganizations();
}
