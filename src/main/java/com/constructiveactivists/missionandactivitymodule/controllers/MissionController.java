package com.constructiveactivists.missionandactivitymodule.controllers;

import com.constructiveactivists.missionandactivitymodule.controllers.configuration.mission.MissionAPI;
import com.constructiveactivists.missionandactivitymodule.controllers.request.mission.MissionRequest;
import com.constructiveactivists.missionandactivitymodule.controllers.request.mission.MissionUpdateRequest;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionTypeEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VolunteerMissionRequirementsEnum;
import com.constructiveactivists.missionandactivitymodule.mappers.mission.MissionMapper;
import com.constructiveactivists.missionandactivitymodule.services.mission.MissionService;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.mission}")
public class MissionController implements MissionAPI {

    private final MissionService missionService;
    private final MissionMapper missionMapper;

    @Override
    public ResponseEntity<List<MissionEntity>> getAllMissions() {
        List<MissionEntity> missions = missionService.getAllMisions();
        return ResponseEntity.ok(missions);
    }

    @Override
    public ResponseEntity<MissionEntity> getMissionById(@PathVariable Integer id) {
        Optional<MissionEntity> mission = missionService.getMissionById(id);
        return mission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<MissionEntity> createMission(@Valid @RequestBody MissionRequest missionRequest) {
        MissionEntity createdMission = missionService.save(missionMapper.toDomain(missionRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMission);
    }

    @Override
    public ResponseEntity<List<MissionEntity>> getMissionsByOrganizationId(@PathVariable Integer organizationId) {
        List<MissionEntity> missions = missionService.getMissionsByOrganizationId(organizationId);
        return ResponseEntity.ok(missions);
    }

    @Override
    public ResponseEntity<List<MissionTypeEnum>> getMissionTypes() {
        return ResponseEntity.ok(missionService.getMissionTypes());
    }

    @Override
    public ResponseEntity<List<VisibilityEnum>> getVisibilityOptions() {
        return ResponseEntity.ok(missionService.getVisibilityOptions());
    }

    @Override
    public ResponseEntity<List<MissionStatusEnum>> getMissionStatusOptions() {
        return ResponseEntity.ok(missionService.getMissionStatusOptions());
    }

    @Override
    public ResponseEntity<List<VolunteerMissionRequirementsEnum>> getVolunteerRequirements() {
        return ResponseEntity.ok(missionService.getVolunteerRequirements());
    }

    @Override
    public ResponseEntity<List<SkillEnum>> getRequiredSkills() {
        return ResponseEntity.ok(missionService.getRequiredSkills());
    }

    @Override
    public ResponseEntity<List<InterestEnum>> getInterests() {
        return ResponseEntity.ok(missionService.getRequiredInterests());
    }

    @Override
    public ResponseEntity<Void> cancelMission(@PathVariable Integer id) {
        try {
            missionService.cancelMissionById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<ActivityEntity>> getAllActivitiesByOrganizationId(@PathVariable Integer organizationId) {
        try {
            List<ActivityEntity> activities = missionService.getAllActivitiesByOrganizationId(organizationId);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<MissionEntity>> getLastThreeMissions() {
        List<MissionEntity> lastThreeMissions = missionService.getLastThreeMissions();
        return ResponseEntity.ok(lastThreeMissions);
    }

    @Override
    public ResponseEntity<MissionEntity> updateMission(Integer id, MissionUpdateRequest missionUpdateRequest) {
        try {
            MissionEntity updatedMission = missionService.updateMission(id, missionMapper.toEntity(missionUpdateRequest));
            return ResponseEntity.ok(updatedMission);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
