package com.constructiveactivists.missionandactivitymanagementmodule.controllers.configuration;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.postulation.PostulationEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Postulación", description = "Operaciones relacionadas con la gestión de postulaciones en la aplicación.")
public interface PostulationAPI {

    @Operation(summary = "Obtener todas las postulaciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping
    ResponseEntity<List<PostulationEntity>> getAllPostulations();

    @Operation(summary = "Obtener una postulación por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Postulación no encontrada")
    })
    @GetMapping("/{id}")
    ResponseEntity<PostulationEntity> getPostulationById(@PathVariable("id") Integer id);

//    @Operation(summary = "Crear una nueva postulación")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Postulación creada"),
//            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
//    })
//    @PostMapping
//    ResponseEntity<PostulationEntity> createPostulation(@RequestBody PostulationEntity postulation);

    @Operation(summary = "Eliminar una postulación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Postulación eliminada"),
            @ApiResponse(responseCode = "404", description = "Postulación no encontrada")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePostulation(@PathVariable("id") Integer id);

    @Operation(summary = "Aprobar una postulación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Postulación aprobada"),
            @ApiResponse(responseCode = "404", description = "Postulación no encontrada")
    })
    @PutMapping("/{id}/approve")
    ResponseEntity<PostulationEntity> approvePostulation(@PathVariable("id") Integer id);
}
