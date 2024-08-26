package com.constructiveactivists.missionandactivitymanagementmodule.controllers;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.configuration.ActivityAPI;
import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.activity.ActivityRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.mappers.ActivityMapper;
import com.constructiveactivists.missionandactivitymanagementmodule.services.ActivityService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.activity}")
public class ActivityController implements ActivityAPI {

    private final ActivityService activityService;
    private final ActivityMapper activityMapper;

    @Override
    public ResponseEntity<List<ActivityEntity>> getAllActivities() {
        List<ActivityEntity> activities = activityService.getAll();
        return ResponseEntity.ok(activities);
    }

    @Override
    public ResponseEntity<ActivityEntity> getActivityById(@PathVariable Integer id) {
        Optional<ActivityEntity> activity = activityService.getById(id);
        return activity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<ActivityEntity> createActivity(@Valid @RequestBody ActivityRequest activityRequest) {
        ActivityEntity createdActivity = activityService.save(activityMapper.toDomain(activityRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdActivity);
    }
}
