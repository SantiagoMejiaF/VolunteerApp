package com.constructiveactivists.missionandactivitymodule.controllers.configuration.mission;

import com.constructiveactivists.missionandactivitymodule.controllers.request.mission.MissionRequest;
import com.constructiveactivists.missionandactivitymodule.controllers.request.mission.MissionUpdateRequest;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionTypeEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VolunteerMissionRequirementsEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(summary = "Obtener todas las misiones de una organización.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/organization/{organizationId}")
    ResponseEntity<List<MissionEntity>> getMissionsByOrganizationId(@PathVariable Integer organizationId);

    @Operation(summary = "Obtener los tipos de misión.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/mission-types")
    ResponseEntity<List<MissionTypeEnum>> getMissionTypes();

    @Operation(summary = "Obtener las opciones de visibilidad.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/visibility-options")
    ResponseEntity<List<VisibilityEnum>> getVisibilityOptions();

    @Operation(summary = "Obtener las opciones de estado de misión.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/mission-status-options")
    ResponseEntity<List<MissionStatusEnum>> getMissionStatusOptions();

    @Operation(summary = "Obtener los requisitos de misión para voluntarios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/volunteer-requirements")
    ResponseEntity<List<VolunteerMissionRequirementsEnum>> getVolunteerRequirements();

    @Operation(summary = "Obtener las habilidades requeridas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/required-skills")
    ResponseEntity<List<SkillEnum>> getRequiredSkills();

    @Operation(summary = "Obtener los intereses.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/interests")
    ResponseEntity<List<InterestEnum>> getInterests();

    @Operation(summary = "Cancelar una misión por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Misión cancelada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Misión no encontrada")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> cancelMission(@PathVariable Integer id);

    @Operation(summary = "Obtener todas las actividades de una organización.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/organization/{organizationId}/activities")
    ResponseEntity<List<ActivityEntity>> getAllActivitiesByOrganizationId(@PathVariable Integer organizationId);

    @Operation(summary = "Obtener las 3 misiones más recientes.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa",
                    content = @Content (schema = @Schema(implementation = MissionEntity.class)))
    })
    @GetMapping("/recent")
    ResponseEntity<List<MissionEntity>> getLastThreeMissions();

    @Operation(summary = "Actualizar una misión existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Misión actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Misión no encontrada"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PutMapping("/{id}")
    ResponseEntity<MissionEntity> updateMission(@PathVariable Integer id, @Valid @RequestBody MissionUpdateRequest missionUpdateRequest);

}
