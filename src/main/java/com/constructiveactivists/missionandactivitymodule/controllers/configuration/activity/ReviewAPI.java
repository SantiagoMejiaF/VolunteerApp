package com.constructiveactivists.missionandactivitymodule.controllers.configuration.activity;

import com.constructiveactivists.missionandactivitymodule.controllers.request.activity.ReviewRequest;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ReviewEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
}
