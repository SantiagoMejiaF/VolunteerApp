package com.constructiveactivists.missionandactivitymanagementmodule.controllers;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.configuration.VolunteerGroupAPI;
import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.volunteergroup.VolunteerGroupRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.volunteergroup.VolunteerGroupEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.mappers.VolunteerGroupMapper;
import com.constructiveactivists.missionandactivitymanagementmodule.services.VolunteerGroupService;
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
@RequestMapping("${request-mapping.controller.volunteer-group}")
public class VolunteerGroupController implements VolunteerGroupAPI {

    private final VolunteerGroupService volunteerGroupService;
    private final VolunteerGroupMapper volunteerGroupMapper;

    @Override
    public ResponseEntity<List<VolunteerGroupEntity>> getAllVolunteerGroups() {
        List<VolunteerGroupEntity> volunteerGroups = volunteerGroupService.getAll();
        return ResponseEntity.ok(volunteerGroups);
    }

    @Override
    public ResponseEntity<VolunteerGroupEntity> getVolunteerGroupById(@PathVariable Integer id) {
        Optional<VolunteerGroupEntity> volunteerGroup = volunteerGroupService.getById(id);
        return volunteerGroup.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<VolunteerGroupEntity> createVolunteerGroup(@Valid @RequestBody VolunteerGroupRequest volunteerGroupRequest) {
        VolunteerGroupEntity createdVolunteerGroup = volunteerGroupService.save(volunteerGroupMapper.toDomain(volunteerGroupRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVolunteerGroup);
    }
}
