package com.constructiveactivists.volunteermanagementmodule.controllers.configuration;

import com.constructiveactivists.volunteermanagementmodule.controllers.request.VolunteerRequest;
import com.constructiveactivists.volunteermanagementmodule.controllers.request.VolunteerUpdateRequest;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.InterestEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.RelationshipEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.SkillEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Gestión de Voluntarios", description = "Operaciones relacionadas con la gestión de voluntarios en la aplicación.")
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


    @Operation(summary = "Obtener la cantidad de voluntarios activos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/active-count")
    ResponseEntity<Long> getActiveVolunteerCount();

    @Operation(summary = "Actualizar la información de un voluntario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
    })
    @PutMapping("/{id}")
    ResponseEntity<VolunteerEntity> updateVolunteer(@PathVariable("id") Integer id, @RequestBody VolunteerUpdateRequest volunteerUpdateRequest);

    @Operation(summary = "Contador de habilidades de todos los voluntarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/skill-counts")
    ResponseEntity<Map<SkillEnum, Integer>> getSkillCounts();

    @Operation(summary = "Obtener la cantidad de voluntarios en cada rango de edad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/age-ranges")
    ResponseEntity<Map<String, Long>> getAgeRanges();

    @Operation(summary = "Obtener la edad promedio de los voluntarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/average-age")
    ResponseEntity<Double> getAverageAge();

    @Operation(summary = "Obtener la cantidad de voluntarios disponibles en cada día de la semana")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/availability-count")
    ResponseEntity<Map<AvailabilityEnum, Long>> getVolunteerAvailabilityCount();

    @Operation(summary = "Obtener la cantidad de voluntarios que tienen cada interés")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/interest-count")
    ResponseEntity<Map<InterestEnum, Long>> getInterestCount();
}
