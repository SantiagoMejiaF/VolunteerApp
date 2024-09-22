package com.constructiveactivists.missionandactivitymodule.controllers;

import com.constructiveactivists.missionandactivitymodule.controllers.configuration.volunteergroup.VolunteerGroupAPI;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupEntity;
import com.constructiveactivists.missionandactivitymodule.mappers.volunteergroup.VolunteerGroupMapper;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
        List<VolunteerGroupEntity> volunteerGroups = volunteerGroupService.getAllVolunteerGroups();
        return ResponseEntity.ok(volunteerGroups);
    }

    @Override
    public ResponseEntity<VolunteerGroupEntity> getVolunteerGroupById(@PathVariable Integer id) {
        Optional<VolunteerGroupEntity> volunteerGroup = volunteerGroupService.getVolunteerGroupById(id);
        return volunteerGroup.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<List<VolunteerGroupEntity>> getVolunteerGroupsByOrganizationId(@PathVariable Integer organizationId) {
        List<VolunteerGroupEntity> volunteerGroups = volunteerGroupService.getVolunteerGroupByOrganizationId(organizationId);
        return ResponseEntity.ok(volunteerGroups);
    }
}
