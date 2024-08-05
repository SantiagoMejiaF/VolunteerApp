package com.constructiveactivists.usermanagementmodule.controllers.volunteer.configuration;

import com.constructiveactivists.usermanagementmodule.controllers.volunteer.request.VolunteerRequest;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.RelationshipEnum;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.SkillEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gestión de Voluntarios", description = "Operaciones relacionadas con la gestión de voluntarios en la aplicación de voluntariado")
public interface VolunteerAPI {

    @Operation(summary = "Obtener una lista con todos los voluntarios registrados en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping
    List<VolunteerEntity> getAllVolunteers();

    @Operation(summary = "Obtener un voluntario por Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<VolunteerEntity> getVolunteerById(@PathVariable("id") Integer id);

    @Operation(summary = "Agregar un voluntario a la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Voluntario creado"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "409", description = "Conflicto: el voluntario ya existe")
    })
    @PostMapping
    ResponseEntity<VolunteerEntity> createVolunteer(@RequestBody VolunteerRequest volunteerRequest);

    @Operation(summary = "Eliminar un voluntario de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Voluntario eliminado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteVolunteer(@PathVariable("id") Integer id);


    @Operation(summary = "Obtener una lista con todos los intereses disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/interests")
    ResponseEntity<List<InterestEnum>> getAllInterests();

    @Operation(summary = "Obtener una lista con todas las habilidades disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/skills")
    ResponseEntity<List<SkillEnum>> getAllSkills();

    @Operation(summary = "Obtener una lista con todas las disponibilidades disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/availabilities")
    ResponseEntity<List<AvailabilityEnum>> getAllAvailabilities();

    @Operation(summary = "Obtener una lista con todas las relaciones disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/relationships")
    ResponseEntity<List<RelationshipEnum>> getAllRelationships();
}
