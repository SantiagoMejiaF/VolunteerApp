package com.constructiveactivists.volunteermanagementmodule.controllers;

import com.constructiveactivists.volunteermanagementmodule.controllers.configuration.VolunteerOrganizationAPI;
import com.constructiveactivists.volunteermanagementmodule.controllers.request.VolunteerOrganizationRequest;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.OrganizationStatusEnum;
import com.constructiveactivists.volunteermanagementmodule.mappers.VolunteerOrganizationMapper;
import com.constructiveactivists.volunteermanagementmodule.services.VolunteerOrganizationService;
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
    public ResponseEntity<VolunteerOrganizationEntity> updateHours(@PathVariable Integer id,
                                                                   @RequestParam Integer hoursCompleted,
                                                                   @RequestParam Integer hoursCertified) {
        VolunteerOrganizationEntity updatedVolunteerOrganizationEntity = volunteerOrganizationService.updateHours(id, hoursCompleted, hoursCertified);
        return ResponseEntity.ok(updatedVolunteerOrganizationEntity);
    }

    @Override
    public ResponseEntity<List<VolunteerOrganizationEntity>> getVolunteersWithStatus(@PathVariable OrganizationStatusEnum status) {
        List<VolunteerOrganizationEntity> volunteers = volunteerOrganizationService.getVolunteersWithStatus(status);
        if (volunteers == null || volunteers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(volunteers);
    }

    @Override
    public ResponseEntity<VolunteerOrganizationEntity> addVolunteerOrganizationPending(@RequestBody VolunteerOrganizationRequest volunteerOrganizationRequest) {
        VolunteerOrganizationEntity volunteerOrganizationEntity = volunteerOrganizationService.addVolunteerOrganizationPending(volunteerOrganizationMapper.toEntity(volunteerOrganizationRequest));
        return ResponseEntity.ok(volunteerOrganizationEntity);
    }
}

