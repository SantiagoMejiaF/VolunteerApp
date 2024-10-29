package com.constructiveactivists.volunteermodule.controllers;

import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.volunteermodule.controllers.configuration.VolunteerOrganizationAPI;
import com.constructiveactivists.volunteermodule.controllers.request.volunteerorganization.VolunteerOrganizationRequest;
import com.constructiveactivists.volunteermodule.controllers.response.StatusVolunteerOrganizationResponse;
import com.constructiveactivists.volunteermodule.controllers.response.VolunteerOrganizationResponse;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.mappers.volunteerorganization.VolunteerOrganizationMapper;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.VolunteerOrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${request-mapping.controller.volunteer-organization}")
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
    @Override
    public ResponseEntity<VolunteerOrganizationResponse> getVolunteerOrganizationDetails(@PathVariable Integer volunteerOrganizationId) {
        VolunteerOrganizationResponse details = volunteerOrganizationService.getVolunteerOrganizationDetails(volunteerOrganizationId);
        return ResponseEntity.ok(details);
    }
    @Override
    public ResponseEntity<List<StatusVolunteerOrganizationResponse>> getPendingVolunteersByOrganizationId(@PathVariable Integer organizationId) {
        List<StatusVolunteerOrganizationResponse> pendingVolunteers = volunteerOrganizationService.getPendingVolunteersByOrganizationId(organizationId);
        return ResponseEntity.ok(pendingVolunteers);
    }

    @Override
    public ResponseEntity<List<StatusVolunteerOrganizationResponse>> getAcceptedVolunteersByOrganizationId(@PathVariable Integer organizationId) {
        List<StatusVolunteerOrganizationResponse> acceptedVolunteers = volunteerOrganizationService.getAcceptedVolunteersByOrganizationId(organizationId);
        return ResponseEntity.ok(acceptedVolunteers);
    }

    @Override
    public ResponseEntity<List<StatusVolunteerOrganizationResponse>> getRejectedVolunteersByOrganizationId(@PathVariable Integer organizationId) {
        List<StatusVolunteerOrganizationResponse> rejectedVolunteers = volunteerOrganizationService.getRejectedVolunteersByOrganizationId(organizationId);
        return ResponseEntity.ok(rejectedVolunteers);
    }

    @Override
    public ResponseEntity<List<OrganizationEntity>> getRecentFiveOrganizationsByVolunteerId(@PathVariable Integer volunteerId) {
        List<OrganizationEntity> organizations = volunteerOrganizationService.getRecentOrganizationsByVolunteerId(volunteerId);
        if (organizations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(organizations);
    }

    @Override
    public ResponseEntity<List<VolunteerEntity>> getRecentAcceptedFiveVolunteersByOrganizationId(
            @PathVariable Integer organizationId) {
        List<VolunteerEntity> acceptedVolunteers = volunteerOrganizationService.findFiveVolunteer(organizationId);
        return new ResponseEntity<>(acceptedVolunteers, acceptedVolunteers.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> countAcceptedVolunteers(@PathVariable Integer organizationId) {
        long count = volunteerOrganizationService.countAcceptedVolunteerOrganizationsByOrganizationId(organizationId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

}
