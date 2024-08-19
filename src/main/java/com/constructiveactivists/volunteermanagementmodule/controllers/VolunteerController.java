package com.constructiveactivists.volunteermanagementmodule.controllers;

import com.constructiveactivists.volunteermanagementmodule.controllers.configuration.VolunteerAPI;
import com.constructiveactivists.volunteermanagementmodule.controllers.request.VolunteerRequest;
import com.constructiveactivists.volunteermanagementmodule.controllers.request.VolunteerUpdateRequest;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.InterestEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.RelationshipEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.SkillEnum;
import com.constructiveactivists.volunteermanagementmodule.mappers.VolunteerMapper;
import com.constructiveactivists.volunteermanagementmodule.mappers.VolunteerUpdateMapper;
import com.constructiveactivists.volunteermanagementmodule.services.VolunteerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.volunteer}")
public class VolunteerController implements VolunteerAPI {

    private final VolunteerService volunteerService;
    private final VolunteerUpdateMapper volunteerUpdateMapper;
    private final VolunteerMapper volunteerMapper;

    @Override
    public List<VolunteerEntity> getAllVolunteers() {
        return volunteerService.getAllVolunteers();
    }

    @Override
    public ResponseEntity<VolunteerEntity> getVolunteerById(@PathVariable Integer id) {
        Optional<VolunteerEntity> volunteer = volunteerService.getVolunteerById(id);
        return volunteer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<VolunteerEntity> getVolunteerByUserId(@PathVariable Integer userId) {
        Optional<VolunteerEntity> volunteer = volunteerService.getVolunteerByUserId(userId);
        return volunteer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<VolunteerEntity> createVolunteer(@Valid @RequestBody VolunteerRequest volunteerRequest) {
        VolunteerEntity createdVolunteerEntity = volunteerService.saveVolunteer(volunteerMapper.toEntity(volunteerRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVolunteerEntity);
    }

    @Override
    public ResponseEntity<Void> deleteVolunteer(@PathVariable Integer id) {
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
    public ResponseEntity<Long> getActiveVolunteerCount() {
        long count = volunteerService.getActiveVolunteerCount();
        return ResponseEntity.ok(count);
    }

    @Override
    public ResponseEntity<VolunteerEntity> updateVolunteer(@PathVariable Integer id, @Valid @RequestBody VolunteerUpdateRequest volunteerUpdateRequest) {
        VolunteerEntity updatedVolunteerEntity = volunteerService.updateVolunteer(id, volunteerUpdateMapper.toEntity(volunteerUpdateRequest));
        return ResponseEntity.ok(updatedVolunteerEntity);
    }

    @Override
    public ResponseEntity<Map<SkillEnum, Integer>> getSkillCounts() {
        Map<SkillEnum, Integer> skillCounts = volunteerService.getSkillCounts();
        return ResponseEntity.ok(skillCounts);
    }

    @Override
    public ResponseEntity<Map<String, Long>> getAgeRanges() {
        Map<String, Long> ageRanges = volunteerService.getAgeRanges();
        return ResponseEntity.ok(ageRanges);
    }

    @Override
    public ResponseEntity<Double> getAverageAge() {
        double averageAge = volunteerService.getAverageAge();
        return ResponseEntity.ok(averageAge);
    }

    @Override
    public ResponseEntity<Map<AvailabilityEnum, Long>> getVolunteerAvailabilityCount() {
        Map<AvailabilityEnum, Long> availabilityCount = volunteerService.getVolunteerAvailabilityCount();
        return ResponseEntity.ok(availabilityCount);
    }

    @Override
    public ResponseEntity<Map<InterestEnum, Long>> getInterestCount() {
        Map<InterestEnum, Long> interestCount = volunteerService.getInterestCount();
        return ResponseEntity.ok(interestCount);
    }
}
