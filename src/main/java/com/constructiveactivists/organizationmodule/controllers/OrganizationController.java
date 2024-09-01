package com.constructiveactivists.organizationmodule.controllers;

import com.constructiveactivists.organizationmodule.controllers.configuration.OrganizationAPI;
import com.constructiveactivists.organizationmodule.controllers.request.organization.OrganizationUpdateRequest;
import com.constructiveactivists.organizationmodule.mappers.organization.OrganizationMapper;
import com.constructiveactivists.organizationmodule.controllers.request.organization.OrganizationRequest;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.entities.organization.enums.OrganizationTypeEnum;
import com.constructiveactivists.organizationmodule.entities.organization.enums.SectorTypeEnum;
import com.constructiveactivists.organizationmodule.entities.organization.enums.VolunteeringTypeEnum;
import com.constructiveactivists.organizationmodule.mappers.organization.OrganizationUpdateMapper;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import com.constructiveactivists.organizationmodule.services.organization.VolunteerApprovalService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.organization}")
public class OrganizationController implements OrganizationAPI {

    private final OrganizationService organizationService;
    private final OrganizationMapper organizationMapper;
    private final VolunteerApprovalService volunteerApprovalService;
    private final OrganizationUpdateMapper organizationUpdateMapper;

    @Override
    public ResponseEntity<List<OrganizationEntity>> getAllOrganizations() {
        List<OrganizationEntity> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(organizations);
    }

    @Override
    public ResponseEntity<OrganizationEntity> getOrganizationById(@PathVariable Integer id) {
        Optional<OrganizationEntity> organization = organizationService.getOrganizationById(id);
        return organization.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<OrganizationEntity> getOrganizationByUserId(@PathVariable Integer userId) {
        Optional<OrganizationEntity> organization = organizationService.getOrganizationByUserId(userId);
        return organization.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<OrganizationEntity> createOrganization(@Valid @RequestBody OrganizationRequest organizationRequest) {
        OrganizationEntity createdOrganizationEntity = organizationService.saveOrganization(organizationMapper.toDomain(organizationRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrganizationEntity);
    }

    @Override
    public ResponseEntity<Void> deleteOrganization(@PathVariable Integer id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<List<SectorTypeEnum>> getAllSectors() {
        List<SectorTypeEnum> sectorTypes = organizationService.getAllSectors();
        if (sectorTypes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(sectorTypes);
    }

    @Override
    public ResponseEntity<List<OrganizationTypeEnum>> getAllOrganizationTypes() {
        List<OrganizationTypeEnum> organizationTypes = organizationService.getAllOrganizationTypes();
        if (organizationTypes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(organizationTypes);
    }

    @Override
    public ResponseEntity<List<VolunteeringTypeEnum>> getAllVolunteeringTypes() {
        List<VolunteeringTypeEnum> volunteeringTypes = organizationService.getAllVolunteeringTypes();
        if (volunteeringTypes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(volunteeringTypes);
    }

    @Override
    public ResponseEntity<OrganizationEntity> updateOrganization(@PathVariable Integer id, @Valid @RequestBody OrganizationUpdateRequest updateRequest) {
        OrganizationEntity updatedOrganization = organizationService.updateOrganization(id, organizationUpdateMapper.toDomain(updateRequest));
        return ResponseEntity.ok(updatedOrganization);
    }

    @Override
    public ResponseEntity<Void> sendVolunteerApprovalResponse(
            @RequestParam Integer volunteerId,
            @RequestParam Integer organizationId,
            @RequestParam boolean approved
    ) {
        volunteerApprovalService.sendVolunteerApprovalResponse(volunteerId, organizationId, approved);
        return ResponseEntity.ok().build();
    }
}
