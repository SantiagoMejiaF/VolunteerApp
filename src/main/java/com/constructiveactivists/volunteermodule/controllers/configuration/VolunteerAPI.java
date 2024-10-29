package com.constructiveactivists.volunteermodule.controllers.configuration;

import com.constructiveactivists.volunteermodule.controllers.request.volunteer.VolunteerRequest;
import com.constructiveactivists.volunteermodule.controllers.request.volunteer.VolunteerUpdateRequest;
import com.constructiveactivists.volunteermodule.controllers.response.RankedOrganizationResponse;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.RelationshipEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Módulo de Voluntarios", description = "Servicios relacionados con la gestión de voluntarios en la aplicación.")
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


    @Operation(summary = "Obtener un voluntario por el ID de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/user/{userId}")
    ResponseEntity<VolunteerEntity> getVolunteerByUserId(@PathVariable("userId") Integer userId);


    @Operation(summary = "Agregar un voluntario a la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Voluntario creado"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "409", description = "Conflicto: el voluntario ya existe")
    })
    @PostMapping
    ResponseEntity<VolunteerEntity> createVolunteer(@Valid @RequestBody VolunteerRequest volunteerRequest);


    @Operation(summary = "Eliminar un voluntario de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Voluntario eliminado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteVolunteer(@PathVariable("id") Integer id);


    @Operation(summary = "Obtener todos los intereses que un voluntario puede tener")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/interests")
    ResponseEntity<List<InterestEnum>> getAllInterests();


    @Operation(summary = "Obtener todas las habilidades que un voluntario puede tener")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/skills")
    ResponseEntity<List<SkillEnum>> getAllSkills();


    @Operation(summary = "Obtener todos los días de la semana en los que un voluntario puede trabajar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/availabilities")
    ResponseEntity<List<AvailabilityEnum>> getAllAvailabilities();


    @Operation(summary = "Obtener todos los posibles parentezcos que un voluntario puede tener")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/relationships")
    ResponseEntity<List<RelationshipEnum>> getAllRelationships();


    @Operation(summary = "Actualizar la información de un voluntario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
    })
    @PutMapping("/{id}")
    ResponseEntity<VolunteerEntity> updateVolunteer(@PathVariable("id") Integer id, @RequestBody VolunteerUpdateRequest volunteerUpdateRequest);


    @Operation(summary = "Promover un voluntario a líder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voluntario promovido a líder"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @PutMapping("/{id}/promote")
    ResponseEntity<VolunteerEntity> promoteVolunteerToLeader(@PathVariable("id") Integer id);


    @Operation(summary = "Inscribir un voluntario en una actividad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voluntario inscrito exitosamente"),
            @ApiResponse(responseCode = "404", description = "No encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto: El voluntario ya está inscrito o el grupo está completo")
    })
    @PostMapping("/{volunteerId}/activities/{activityId}/signup")
    ResponseEntity<String> signUpForActivity(@PathVariable("volunteerId") Integer volunteerId, @PathVariable("activityId") Integer activityId);


    @Operation(summary = "Realizar un match entre un voluntario y las organizaciones basadas en intereses y habilidades")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(schema = @Schema(implementation = RankedOrganizationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Voluntario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{volunteerId}/match-organizations")
    ResponseEntity<List<RankedOrganizationResponse>> matchVolunteerWithOrganizations(
            @PathVariable("volunteerId") Integer volunteerId,
            @RequestParam(name = "numberOfMatches") int numberOfMatches);


    @Operation(summary = "Eliminar la inscripción de un voluntario de una actividad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voluntario eliminado de la actividad exitosamente"),
            @ApiResponse(responseCode = "404", description = "No encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto: Solo se permite la eliminación 2 días antes de la actividad")
    })
    @DeleteMapping("/{volunteerId}/activities/{activityId}/remove")
    ResponseEntity<String> removeVolunteerFromActivity(@PathVariable("volunteerId") Integer volunteerId, @PathVariable("activityId") Integer activityId);
}
