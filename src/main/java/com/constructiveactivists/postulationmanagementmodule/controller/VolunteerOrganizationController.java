package com.constructiveactivists.postulationmanagementmodule.controller;

import com.constructiveactivists.postulationmanagementmodule.controller.configuration.VolunteerOrganizationAPI;
import com.constructiveactivists.postulationmanagementmodule.controller.request.VolunteerOrganizationRequest;
import com.constructiveactivists.postulationmanagementmodule.entities.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.OrganizationStatusEnum;
import com.constructiveactivists.postulationmanagementmodule.mapper.VolunteerOrganizationMapper;
import com.constructiveactivists.postulationmanagementmodule.services.VolunteerOrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/volunteer-organizations")
@AllArgsConstructor
public class VolunteerOrganizationController implements VolunteerOrganizationAPI {

    private final VolunteerOrganizationService volunteerOrganizationService;
    private final VolunteerOrganizationMapper volunteerOrganizationMapper;

    @Override
    public ResponseEntity<List<VolunteerOrganizationEntity>> getOrganizationsByVolunteerId(@PathVariable Integer volunteerId) {
        List<VolunteerOrganizationEntity> volunteerOrganizationEntities = volunteerOrganizationService.getOrganizationsByVolunteerId(volunteerId);
        return ResponseEntity.ok(volunteerOrganizationEntities);
    }

    @Override
    public ResponseEntity<List<VolunteerOrganizationEntity>> getVolunteersByOrganizationId(@PathVariable Integer organizationId) {
        List<VolunteerOrganizationEntity> volunteerOrganizationEntities = volunteerOrganizationService.getVolunteersByOrganizationId(organizationId);
        return ResponseEntity.ok(volunteerOrganizationEntities);
    }

    @Override
    public ResponseEntity<VolunteerOrganizationEntity> addVolunteerOrganizationPending(@RequestBody VolunteerOrganizationRequest volunteerOrganizationRequest) {
        VolunteerOrganizationEntity volunteerOrganizationEntity = volunteerOrganizationService.addVolunteerOrganizationPending(volunteerOrganizationMapper.toEntity(volunteerOrganizationRequest));
        return ResponseEntity.ok(volunteerOrganizationEntity);
    }
}

