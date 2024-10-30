package com.constructiveactivists.dashboardsandreportsmodule.controllers;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.dashboardsandreportsmodule.controllers.response.CardsOrganizationVolunteerResponse;
import com.constructiveactivists.dashboardsandreportsmodule.controllers.response.VolunteerInfoResponse;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.dashboardsandreportsmodule.controllers.configuration.DashboardVolunteerAPI;
import com.constructiveactivists.dashboardsandreportsmodule.services.DashboardVolunteerService;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;
import java.util.List;
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

    @Override
    public ResponseEntity<List<CardsOrganizationVolunteerResponse>> getFoundationsByVolunteerId(@PathVariable Integer volunteerId) {
        List<CardsOrganizationVolunteerResponse> foundations = dashboardService.getFoundationsByVolunteerId(volunteerId);
        return ResponseEntity.ok(foundations);
    }

    @Override
    public ResponseEntity<List<VolunteerEntity>> getTenRecentVolunteers() {
        try {
            List<VolunteerEntity> recentVolunteers = dashboardService.getTenRecentVolunteers();
            return ResponseEntity.ok(recentVolunteers);
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<Map<Month, Long>> getVolunteersCountByMonth(@PathVariable int year) {
        Map<Month, Long> volunteersByMonth = dashboardService.getVolunteersCountByMonth(year);
        return ResponseEntity.ok(volunteersByMonth);
    }

    @Override
    public ResponseEntity<List<VolunteerInfoResponse>> getVolunteersByActivity(@PathVariable Integer activityId) {
        List<VolunteerInfoResponse> volunteerInfoList = dashboardService.getVolunteersInfoByActivityId(activityId);
        if (volunteerInfoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(volunteerInfoList);
    }
}
