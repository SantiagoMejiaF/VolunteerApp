package com.constructiveactivists.volunteermodule.controllers;

import com.constructiveactivists.volunteermodule.controllers.configuration.PostulationAPI;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${request-mapping.controller.postulation}")
@AllArgsConstructor
public class PostulationController implements PostulationAPI {

    private final PostulationService postulationService;

    @Override
    public ResponseEntity<List<PostulationEntity>> getPendingPostulationsByOrganizationId(@PathVariable Integer organizationId) {
        List<PostulationEntity> pendingPostulations = postulationService.getPendingPostulationsByOrganizationId(organizationId);
        return ResponseEntity.ok(pendingPostulations);
    }

    @Override
    public ResponseEntity<List<PostulationEntity>> getAcceptedPostulationsByOrganizationId(@PathVariable Integer organizationId) {
        List<PostulationEntity> pendingPostulations = postulationService.getAcceptedPostulationsByOrganizationId(organizationId);
        return ResponseEntity.ok(pendingPostulations);
    }

    @Override
    public ResponseEntity<List<PostulationEntity>> getRefusedPostulationsByOrganizationId(@PathVariable Integer organizationId) {
        List<PostulationEntity> pendingPostulations = postulationService.getRefusedPostulationsByOrganizationId(organizationId);
        return ResponseEntity.ok(pendingPostulations);
    }

    @Override
    public ResponseEntity<PostulationEntity> getPostulationById(@PathVariable Integer volunteerOrganizationId) {
        PostulationEntity postulation = postulationService.findById(volunteerOrganizationId);
        return ResponseEntity.ok(postulation);
    }

}
