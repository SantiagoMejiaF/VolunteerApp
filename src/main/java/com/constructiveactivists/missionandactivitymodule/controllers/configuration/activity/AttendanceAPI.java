package com.constructiveactivists.missionandactivitymodule.controllers.configuration.activity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Tag(name = "Módulo de Misiones y Actividades", description = "Servicios relacionados con la gestión de misiones y actividades en la aplicación.")
public interface AttendanceAPI {

    @Operation(summary = "Redireccionar a Google para hacer check-in", description = "Redirecciona al usuario a Google para hacer check-in en una actividad.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirección exitosa a Google para autenticación"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, el ID de la actividad es inválido o está ausente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar redirigir a Google")
    })
    @GetMapping("/google/auth/checkin")
    void redirectToGoogleForCheckIn(@RequestParam("activityId") Integer activityId, HttpServletResponse response) throws IOException;

    @Operation(
            summary = "Redireccionar a Google para hacer check-out",
            description = "Redirecciona al usuario a la página de autenticación de Google para hacer check-out en una actividad específica. Una vez autenticado, el usuario será redirigido de vuelta a la aplicación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirección exitosa a Google para autenticación"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, el ID de la actividad es inválido o está ausente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar redirigir a Google")
    })
    @GetMapping("/google/auth/checkout")
    void redirectToGoogleForCheckOut(@RequestParam("activityId") Integer activityId, HttpServletResponse response) throws IOException;

    @Operation(
            summary = "Callback de Google para check-in",
            description = "Este endpoint recibe el código de autorización de Google para hacer check-in en una actividad. Intercambia el código por un token de acceso, valida el usuario y registra el check-in en la actividad especificada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check-in registrado exitosamente", content = @Content(mediaType = "text/html")),
            @ApiResponse(responseCode = "400", description = "Error en el check-in, probablemente por datos inválidos", content = @Content(mediaType = "text/html")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar el check-in", content = @Content(mediaType = "text/html"))
    })
    @GetMapping("/google/checkin")
    ResponseEntity<String> googleCheckInCallback(
            @RequestParam("code") String authorizationCode,
            @RequestParam("state") Integer activityId);

    @Operation(
            summary = "Callback de Google para check-out",
            description = "Este endpoint recibe el código de autorización de Google para hacer check-out de una actividad. Intercambia el código por un token de acceso, valida el usuario y registra el check-out en la actividad especificada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check-out registrado exitosamente", content = @Content(mediaType = "text/html")),
            @ApiResponse(responseCode = "400", description = "Error en el check-out, probablemente por datos inválidos", content = @Content(mediaType = "text/html")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar el check-out", content = @Content(mediaType = "text/html"))
    })
    @GetMapping("/google/checkout")
    ResponseEntity<String> googleCheckOutCallback(
            @RequestParam("code") String authorizationCode,
            @RequestParam("state") Integer activityId);


}
