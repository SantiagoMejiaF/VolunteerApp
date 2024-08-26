package com.constructiveactivists.resportsanddashboardsmodule.controllers;

import com.constructiveactivists.resportsanddashboardsmodule.controllers.configuration.DashboardVolunteerAPI;
import com.constructiveactivists.resportsanddashboardsmodule.services.DashboardVolunteerService;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.InterestEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.SkillEnum;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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


}
