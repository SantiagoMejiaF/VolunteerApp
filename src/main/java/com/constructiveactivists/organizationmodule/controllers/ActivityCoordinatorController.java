package com.constructiveactivists.organizationmodule.controllers;

import com.constructiveactivists.organizationmodule.controllers.configuration.ActivityCoordinatorAPI;
import com.constructiveactivists.organizationmodule.controllers.request.activitycoordinator.ActivityCoordinatorRequest;
import com.constructiveactivists.organizationmodule.controllers.request.activitycoordinator.ActivityCoordinatorUpdateRequest;
import com.constructiveactivists.organizationmodule.controllers.request.activitycoordinator.CoordinatorAvailabilityRequest;
import com.constructiveactivists.organizationmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import com.constructiveactivists.organizationmodule.mappers.activitycoordinator.ActivityCoordinatorMapper;
import com.constructiveactivists.organizationmodule.mappers.activitycoordinator.CoordinatorAvailabilityMapper;
import com.constructiveactivists.organizationmodule.services.activitycoordinator.ActivityCoordinatorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.activity-coordinator}")
public class ActivityCoordinatorController implements ActivityCoordinatorAPI {

    private final ActivityCoordinatorService activityCoordinatorService;
    private final ActivityCoordinatorMapper activityCoordinatorMapper;
    private final CoordinatorAvailabilityMapper coordinatorAvailabilityMapper;

    @Override
    public ResponseEntity<List<ActivityCoordinatorEntity>> getAllActivityCoordinators() {
        List<ActivityCoordinatorEntity> coordinators = activityCoordinatorService.getAll();
        return ResponseEntity.ok(coordinators);
    }

    @Override
    public ResponseEntity<ActivityCoordinatorEntity> createActivityCoordinator(@Valid @RequestBody ActivityCoordinatorRequest request) {
        ActivityCoordinatorEntity createdCoordinator = activityCoordinatorService.save(activityCoordinatorMapper.toDomain(request), request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCoordinator);
    }

    @Override
    public ResponseEntity<Void> deleteActivityCoordinator(@PathVariable Integer id) {
        activityCoordinatorService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<List<ActivityCoordinatorEntity>> findAvailableCoordinators(
            @RequestParam("organizationId") Integer organizationId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) String startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) String endTime) {
        CoordinatorAvailabilityRequest request = new CoordinatorAvailabilityRequest();
        request.setOrganizationId(organizationId);
        request.setDate(date);
        request.setStartTime(startTime);
        request.setEndTime(endTime);

        List<ActivityCoordinatorEntity> coordinators = activityCoordinatorService.findAvailableCoordinators(coordinatorAvailabilityMapper.toDomain(request));
        return ResponseEntity.ok(coordinators);
    }

    @Override
    public ResponseEntity<List<ActivityCoordinatorEntity>> getCoordinatorsByOrganization(@PathVariable Integer organizationId) {
        List<ActivityCoordinatorEntity> coordinators = activityCoordinatorService.findByOrganizationId(organizationId);
        return ResponseEntity.ok(coordinators);
    }

    @Override
    public ResponseEntity<ActivityCoordinatorEntity> getCoordinatorById(@PathVariable Integer id) {
        Optional<ActivityCoordinatorEntity> coordinator = activityCoordinatorService.getCoordinatorById(id);
        return coordinator.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<ActivityCoordinatorEntity> getActivityCoordinatorByUserId(@PathVariable Integer userId) {
        try {
            ActivityCoordinatorEntity coordinator = activityCoordinatorService.getActivityCoordinatorByUserId(userId);
            return ResponseEntity.ok(coordinator);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @Override
    public ResponseEntity<ActivityCoordinatorEntity> updateActivityCoordinator(Integer id, ActivityCoordinatorUpdateRequest request) {
        ActivityCoordinatorEntity updatedCoordinator = activityCoordinatorService.updateCoordinatorInfo(id, request);
        return ResponseEntity.ok(updatedCoordinator);
    }
}
