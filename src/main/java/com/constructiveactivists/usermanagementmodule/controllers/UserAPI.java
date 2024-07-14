package com.constructiveactivists.usermanagementmodule.controllers;

import com.constructiveactivists.usermanagementmodule.controllers.request.UserRequest;
import com.constructiveactivists.usermanagementmodule.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@Tag(name = "Gestión de Usuarios y Perfiles", description = "Operaciones relacionadas con la gestión de usuarios en la aplicación de voluntariado")
public interface UserAPI {

    @Operation(summary = "Ver una lista de usuarios disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping
    List<User> getAllUsers();

    @Operation(summary = "Obtener un usuario por Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<User> getUserById(@PathVariable Integer id);

    @Operation(summary = "Agregar un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido")
    })
    @PostMapping
    ResponseEntity<User> createUser(@RequestBody UserRequest user);

    @Operation(summary = "Eliminar un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Integer id);
}

