package com.constructiveactivists.missionandactivitymanagementmodule.controllers;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.configuration.ActivityCoordinatorAPI;
import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.activity.ActivityCoordinatorRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.activity.CoordinatorAvailabilityRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.mappers.ActivityCoordinatorMapper;
import com.constructiveactivists.missionandactivitymanagementmodule.mappers.CoordinatorAvailabilityMapper;
import com.constructiveactivists.missionandactivitymanagementmodule.services.ActivityCoordinatorService;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

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
        UserEntity user = new UserEntity();
        user.setFirstName(request.getNameActivityCoordinator());
        user.setLastName(request.getLastNameActivityCoordinator());
        user.setEmail(request.getEmailActivityCoordinator());
        user.setImage(request.getImage());

        ActivityCoordinatorEntity createdCoordinator = activityCoordinatorService.save(activityCoordinatorMapper.toDomain(request), user);
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
}
