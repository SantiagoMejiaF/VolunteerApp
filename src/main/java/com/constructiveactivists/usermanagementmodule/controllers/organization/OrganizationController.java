package com.constructiveactivists.usermanagementmodule.controllers.organization;

import com.constructiveactivists.usermanagementmodule.controllers.organization.mappers.OrganizationMapper;
import com.constructiveactivists.usermanagementmodule.controllers.organization.request.OrganizationRequest;
import com.constructiveactivists.usermanagementmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.usermanagementmodule.services.OrganizationService;
import com.constructiveactivists.usermanagementmodule.controllers.organization.configuration.OrganizationAPI;
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
@CrossOrigin(origins = "https://volunteer-app.online")
public class OrganizationController implements OrganizationAPI {

    private final OrganizationService organizationService;

    private final OrganizationMapper organizationMapper;

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
    public ResponseEntity<OrganizationEntity> updateOrganization(Integer id, OrganizationRequest organizationRequest) {
        return null;
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
}