package com.constructiveactivists.missionandactivitymodule.controllers;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.missionandactivitymodule.controllers.configuration.activity.ActivityAPI;
import com.constructiveactivists.missionandactivitymodule.controllers.request.activity.ActivityRequest;
import com.constructiveactivists.missionandactivitymodule.controllers.request.activity.ActivityUpdateRequest;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.mappers.activity.ActivityMapper;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @Override
    public ResponseEntity<byte[]> getCheckInQrCode(@PathVariable Integer activityId) {
        byte[] qrCode = activityService.getCheckInQrCode(activityId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getCheckOutQrCode(@PathVariable Integer activityId) {
        byte[] qrCode = activityService.getCheckOutQrCode(activityId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ActivityEntity>> getActivitiesByMissionId(@PathVariable Integer missionId) {
        List<ActivityEntity> activities = activityService.getActivitiesByMissionId(missionId);
        return ResponseEntity.ok(activities);
    }

    @Override
    public ResponseEntity<String> deleteActivity(@PathVariable Integer id) {
        try {
            activityService.deleteActivityById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Activity with ID " + id + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while trying to delete the activity.");
        }
    }

    @Override
    public ResponseEntity<List<ActivityEntity>> getVolunteerActivities(@PathVariable Integer volunteerId) {
        List<ActivityEntity> activities = activityService.getActivitiesByVolunteerId(volunteerId);
        return ResponseEntity.ok(activities);
    }

    @Override
    public ResponseEntity<List<ActivityEntity>> getActivitiesByCoordinator(Integer coordinatorId) {
        List<ActivityEntity> activities = activityService.findAllByActivityCoordinator(coordinatorId);
        return ResponseEntity.ok(activities);
    }

    @Override
    public ResponseEntity<List<ActivityEntity>> getAvailableActivitiesByCoordinator(Integer coordinatorId) {
        List<ActivityEntity> availableActivities = activityService.getAvailableActivitiesByCoordinator(coordinatorId);
        return ResponseEntity.ok(availableActivities);
    }

    @Override
    public ResponseEntity<Integer> getCompletedActivitiesCountVolunteer(@PathVariable Integer volunteerId) {
        int count = activityService.getCompletedActivitiesCountVolunteer(volunteerId);
        return ResponseEntity.ok(count);
    }

    @Override
    public ResponseEntity<Integer> getTotalBeneficiariesImpacted(@PathVariable Integer volunteerId) {
        int totalBeneficiaries = activityService.getTotalBeneficiariesImpactedByVolunteer(volunteerId);
        return ResponseEntity.ok(totalBeneficiaries);
    }

    @Override
    public ResponseEntity<List<ActivityEntity>> getActivitiesByVolunteerAndDate(
            @PathVariable Integer volunteerId,
            @RequestParam int month,
            @RequestParam int year) {
        List<ActivityEntity> activities = activityService.getActivitiesByVolunteerAndDate(volunteerId, month, year);
        return ResponseEntity.ok(activities);
    }

    @Override
    public ResponseEntity<Double> getAverageRating(@PathVariable Integer volunteerId) {
        try {
            double averageRating = activityService.getAverageRatingByVolunteer(volunteerId);
            return new ResponseEntity<>(averageRating, HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Override
    public ResponseEntity<Map<String, Long>> getActivitiesCountByVolunteerAndYear(
            @PathVariable Integer volunteerId,
            @PathVariable int year) {
        Map<String, Long> activitiesCount = activityService.getActivitiesCountByVolunteerAndYearInSpanish(volunteerId, year);
        return ResponseEntity.ok(activitiesCount);
    }

    @Override
    public ResponseEntity<ActivityEntity> updateActivity(Integer id, ActivityUpdateRequest activityUpdateRequest) {
        try {
            ActivityEntity updatedActivity = activityService.updateActivity(id, activityMapper.toEntity(activityUpdateRequest));
            return ResponseEntity.ok(updatedActivity);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
