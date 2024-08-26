package com.constructiveactivists.missionandactivitymanagementmodule.controllers;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.configuration.ActivityCoordinatorAPI;
import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.activity.ActivityCoordinatorRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.mappers.ActivityCoordinatorMapper;
import com.constructiveactivists.missionandactivitymanagementmodule.services.ActivityCoordinatorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.activity-coordinator}")
public class ActivityCoordinatorController implements ActivityCoordinatorAPI {

    private final ActivityCoordinatorService activityCoordinatorService;
    private final ActivityCoordinatorMapper activityCoordinatorMapper;

    @Override
    public ResponseEntity<List<ActivityCoordinatorEntity>> getAllActivityCoordinators() {
        List<ActivityCoordinatorEntity> coordinators = activityCoordinatorService.getAll();
        return ResponseEntity.ok(coordinators);
    }

    @Override
    public ResponseEntity<ActivityCoordinatorEntity> createActivityCoordinator(@Valid @RequestBody ActivityCoordinatorRequest request) {
        ActivityCoordinatorEntity createdCoordinator = activityCoordinatorService.save(activityCoordinatorMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCoordinator);
    }

    @Override
    public ResponseEntity<Void> deleteActivityCoordinator(@PathVariable Integer id) {
        activityCoordinatorService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
