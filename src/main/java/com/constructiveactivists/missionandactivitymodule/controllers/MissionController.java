package com.constructiveactivists.missionandactivitymodule.controllers;

import com.constructiveactivists.missionandactivitymodule.controllers.configuration.mission.MissionAPI;
import com.constructiveactivists.missionandactivitymodule.controllers.request.mission.MissionRequest;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.mappers.mission.MissionMapper;
import com.constructiveactivists.missionandactivitymodule.services.mission.MissionService;
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
}
