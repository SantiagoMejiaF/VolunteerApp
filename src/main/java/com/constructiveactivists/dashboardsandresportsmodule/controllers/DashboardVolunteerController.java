package com.constructiveactivists.dashboardsandresportsmodule.controllers;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.dashboardsandresportsmodule.controllers.configuration.DashboardVolunteerAPI;
import com.constructiveactivists.dashboardsandresportsmodule.services.DashboardVolunteerService;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.volunteer-dashboard}")
public class DashboardVolunteerController implements DashboardVolunteerAPI {

    private final DashboardVolunteerService dashboardService;

    @Override
    public ResponseEntity<Map<String, Long>> getAgeRanges() {
        Map<String, Long> ageRanges = dashboardService.getAgeRanges();
        return ResponseEntity.ok(ageRanges);
    }

    @Override
    public ResponseEntity<Double> getAverageAge() {
        double averageAge = dashboardService.getAverageAge();
        return ResponseEntity.ok(averageAge);
    }

    @Override
    public ResponseEntity<Long> getActiveVolunteerCount() {
        long count = dashboardService.getActiveVolunteerCount();
        return ResponseEntity.ok(count);
    }

    @Override
    public ResponseEntity<Map<SkillEnum, Integer>> getSkillCounts() {
        Map<SkillEnum, Integer> skillCounts = dashboardService.getSkillCounts();
        return ResponseEntity.ok(skillCounts);
    }

    @Override
    public ResponseEntity<Map<AvailabilityEnum, Long>> getVolunteerAvailabilityCount() {
        Map<AvailabilityEnum, Long> availabilityCount = dashboardService.getVolunteerAvailabilityCount();
        return ResponseEntity.ok(availabilityCount);
    }

    @Override
    public ResponseEntity<Map<InterestEnum, Long>> getInterestCount() {
        Map<InterestEnum, Long> interestCount = dashboardService.getInterestCount();
        return ResponseEntity.ok(interestCount);
    }

    @Override
    public ResponseEntity<ActivityEntity> getNextActivityForVolunteer(@PathVariable Integer volunteerId) {
        try {
            ActivityEntity nextActivity = dashboardService.getNextActivityForVolunteer(volunteerId);
            return nextActivity != null ? ResponseEntity.ok(nextActivity) : ResponseEntity.noContent().build();
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
