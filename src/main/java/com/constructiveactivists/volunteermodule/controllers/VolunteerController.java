package com.constructiveactivists.volunteermodule.controllers;

import com.constructiveactivists.volunteermodule.controllers.configuration.VolunteerAPI;
import com.constructiveactivists.volunteermodule.controllers.request.volunteer.VolunteerRequest;
import com.constructiveactivists.volunteermodule.controllers.request.volunteer.VolunteerUpdateRequest;
import com.constructiveactivists.volunteermodule.controllers.response.RankedOrganizationResponse;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.RelationshipEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import com.constructiveactivists.volunteermodule.mappers.volunteer.RankedOrganizationMapper;
import com.constructiveactivists.volunteermodule.mappers.volunteer.VolunteerMapper;
import com.constructiveactivists.volunteermodule.mappers.volunteer.VolunteerUpdateMapper;
import com.constructiveactivists.volunteermodule.models.RankedOrganization;
import com.constructiveactivists.volunteermodule.services.volunteer.VolunteerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.volunteer}")
public class VolunteerController implements VolunteerAPI {

    private final VolunteerService volunteerService;
    private final VolunteerUpdateMapper volunteerUpdateMapper;
    private final VolunteerMapper volunteerMapper;
    private final RankedOrganizationMapper rankedOrganizationMapper;

    @Override
    public List<VolunteerEntity> getAllVolunteers() {
        return volunteerService.getAllVolunteers();
    }

    @Override
    public ResponseEntity<VolunteerEntity> getVolunteerById(Integer id) {
        Optional<VolunteerEntity> volunteer = volunteerService.getVolunteerById(id);
        return volunteer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<VolunteerEntity> getVolunteerByUserId(Integer userId) {
        Optional<VolunteerEntity> volunteer = volunteerService.getVolunteerByUserId(userId);
        return volunteer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<VolunteerEntity> createVolunteer(@Valid @RequestBody VolunteerRequest volunteerRequest) {
        VolunteerEntity createdVolunteerEntity = volunteerService.saveVolunteer(volunteerMapper.toEntity(volunteerRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVolunteerEntity);
    }

    @Override
    public ResponseEntity<Void> deleteVolunteer(Integer id) {
        volunteerService.deleteVolunteer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<List<InterestEnum>> getAllInterests() {
        List<InterestEnum> interests = volunteerService.getAllInterests();
        return ResponseEntity.ok(interests);
    }

    @Override
    public ResponseEntity<List<SkillEnum>> getAllSkills() {
        List<SkillEnum> skills = volunteerService.getAllSkills();
        return ResponseEntity.ok(skills);
    }

    @Override
    public ResponseEntity<List<AvailabilityEnum>> getAllAvailabilities() {
        List<AvailabilityEnum> availabilities = volunteerService.getAllAvailabilities();
        return ResponseEntity.ok(availabilities);
    }

    @Override
    public ResponseEntity<List<RelationshipEnum>> getAllRelationships() {
        List<RelationshipEnum> relationships = volunteerService.getAllRelationships();
        return ResponseEntity.ok(relationships);
    }

    @Override
    public ResponseEntity<VolunteerEntity> updateVolunteer(Integer id, @Valid @RequestBody VolunteerUpdateRequest volunteerUpdateRequest) {
        VolunteerEntity updatedVolunteerEntity = volunteerService.updateVolunteer(id, volunteerUpdateMapper.toEntity(volunteerUpdateRequest));
        return ResponseEntity.ok(updatedVolunteerEntity);
    }

    @Override
    public ResponseEntity<VolunteerEntity> promoteVolunteerToLeader(Integer id) {
        VolunteerEntity updatedVolunteer = volunteerService.promoteToLeader(id);
        return ResponseEntity.ok(updatedVolunteer);
    }

    @Override
    public ResponseEntity<String> signUpForActivity(Integer volunteerId,Integer activityId) {
        volunteerService.signUpForActivity(volunteerId, activityId);
        return ResponseEntity.ok("Voluntario inscrito exitosamente");
    }

    @Override
    public ResponseEntity<List<RankedOrganizationResponse>> matchVolunteerWithOrganizations(Integer volunteerId, int numberOfMatches) {
        try {
            List<RankedOrganization> rankedOrganizations = volunteerService.matchVolunteerWithMissions(volunteerId, numberOfMatches);
            List<RankedOrganizationResponse> response = rankedOrganizationMapper.toResponses(rankedOrganizations);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<String> removeVolunteerFromActivity(Integer volunteerId, Integer activityId) {
        volunteerService.removeVolunteerFromActivity(volunteerId, activityId);
        return ResponseEntity.ok("Voluntario eliminado de la actividad exitosamente");
    }
}
