package com.constructiveactivists.volunteermodule.controllers;

import com.constructiveactivists.volunteermodule.controllers.configuration.VolunteerOrganizationAPI;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.VolunteerOrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${request-mapping.controller.volunteer-organization}")
@AllArgsConstructor
public class VolunteerOrganizationController implements VolunteerOrganizationAPI {

    private final VolunteerOrganizationService volunteerOrganizationService;

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
}
