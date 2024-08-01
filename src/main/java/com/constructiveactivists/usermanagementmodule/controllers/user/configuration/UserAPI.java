package com.constructiveactivists.usermanagementmodule.controllers.user.configuration;

import com.constructiveactivists.usermanagementmodule.controllers.user.request.TokenRequest;
import com.constructiveactivists.usermanagementmodule.controllers.user.request.UserRequest;
import com.constructiveactivists.usermanagementmodule.entities.user.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@Tag(name = "Gestión de Usuarios Prueba", description = "Operaciones relacionadas con la gestión de usuarios en la aplicación de voluntariado")
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

    @Operation(summary = "Agregar un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido")
    })
    @PostMapping
    ResponseEntity<UserEntity> createUser(@RequestBody UserRequest user);

    @Operation(summary = "Eliminar un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Integer id);

    @Operation(summary = "Verificar token de Google")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token de Google válido"),
            @ApiResponse(responseCode = "400", description = "Token de Google inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/google")
    ResponseEntity<UserEntity> google(@RequestBody TokenRequest tokenDto) throws IOException;

}

