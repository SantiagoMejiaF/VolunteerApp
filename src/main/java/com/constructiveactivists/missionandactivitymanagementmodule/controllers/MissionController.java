package com.constructiveactivists.missionandactivitymanagementmodule.controllers;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.configuration.MissionAPI;
import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.MissionRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.MissionEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.mappers.MissionMapper;
import com.constructiveactivists.missionandactivitymanagementmodule.services.MissionService;
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
        List<MissionEntity> missions = missionService.getAll();
        return ResponseEntity.ok(missions);
    }

    @Override
    public ResponseEntity<MissionEntity> getMissionById(@PathVariable Integer id) {
        Optional<MissionEntity> mission = missionService.getById(id);
        return mission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<MissionEntity> createMission(@Valid @RequestBody MissionRequest missionRequest) {
        MissionEntity createdMission = missionService.save(missionMapper.toDomain(missionRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMission);
    }
}
