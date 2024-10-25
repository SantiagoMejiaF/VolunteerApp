package com.constructiveactivists.missionandactivitymodule.controllers.configuration.activity;

import com.constructiveactivists.missionandactivitymodule.controllers.request.activity.ReviewRequest;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ReviewEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Módulo de Misiones y Actividades", description = "Servicios relacionados con la gestión de misiones y actividades en la aplicación.")
public interface ReviewAPI {


    @Operation(summary = "Crear reseña para una actividad", description = "Crea una reseña para una actividad específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada")
    })
    @GetMapping("/review")
    ResponseEntity<String> createReviewForActivity(
            @RequestParam("activityId") Integer activityId,
            @ModelAttribute ReviewRequest reviewRequest);

    @Operation(summary = "Obtener reseñas por ID de voluntario", description = "Obtiene una lista de reseñas asociadas a un voluntario específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reseñas obtenidas exitosamente"),
            @ApiResponse(responseCode = "404", description = "Voluntario no encontrado")
    })
    @GetMapping("/history/volunteer/{volunteerId}")
    ResponseEntity<List<ReviewEntity>> getReviewsByVolunteerId(@PathVariable Integer volunteerId);
}
