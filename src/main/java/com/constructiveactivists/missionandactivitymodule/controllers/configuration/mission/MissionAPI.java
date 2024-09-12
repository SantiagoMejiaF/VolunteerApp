package com.constructiveactivists.missionandactivitymodule.controllers.configuration.mission;

import com.constructiveactivists.missionandactivitymodule.controllers.request.mission.MissionRequest;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Módulo de Misiones y Actividades", description = "Servicios relacionados con la gestión de misiones y actividades en la aplicación.")
public interface MissionAPI {

    @Operation(summary = "Crear una nueva misión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Misión creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    ResponseEntity<MissionEntity> createMission(@Valid @RequestBody MissionRequest missionRequest);


    @Operation(summary = "Obtener una misión por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Misión no encontrada")
    })
    @GetMapping("/{id}")
    ResponseEntity<MissionEntity> getMissionById(@PathVariable Integer id);


    @Operation(summary = "Obtener todas las misiones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping
    ResponseEntity<List<MissionEntity>> getAllMissions();
}