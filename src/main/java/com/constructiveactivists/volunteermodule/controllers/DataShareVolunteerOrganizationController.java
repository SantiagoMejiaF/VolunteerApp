package com.constructiveactivists.volunteermodule.controllers;

import com.constructiveactivists.volunteermodule.controllers.configuration.DataShareVolunteerOrganizationAPI;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.DataShareVolunteerOrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${request-mapping.controller.datashare-volunteer-organization}")
@AllArgsConstructor
public class DataShareVolunteerOrganizationController implements DataShareVolunteerOrganizationAPI {

    private final DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;

    @Override
    public ResponseEntity<List<DataShareVolunteerOrganizationEntity>> findAll() {
        List<DataShareVolunteerOrganizationEntity> entities = dataShareVolunteerOrganizationService.findAll();
        return ResponseEntity.ok(entities);
    }
}
