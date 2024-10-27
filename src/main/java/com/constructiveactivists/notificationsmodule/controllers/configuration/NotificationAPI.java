package com.constructiveactivists.notificationsmodule.controllers.configuration;

import com.constructiveactivists.notificationsmodule.entities.NotificationEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Módulo de Usuarios", description = "Servicios relacionados con la gestión de usuarios en la aplicación.")
public interface NotificationAPI {


    @Operation(summary = "Obtener las notificaciones de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/{userId}")
    ResponseEntity<List<NotificationEntity>> getUserNotifications(@PathVariable Integer userId);

    @Operation(summary = "Obtener las 5 notificaciones más recientes de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/recent/{userId}")
    ResponseEntity<List<NotificationEntity>> getMostRecentUserNotifications(@PathVariable Integer userId);
}
