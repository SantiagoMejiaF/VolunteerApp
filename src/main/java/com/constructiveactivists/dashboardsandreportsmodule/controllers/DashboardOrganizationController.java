package com.constructiveactivists.dashboardsandreportsmodule.controllers;

import com.constructiveactivists.dashboardsandreportsmodule.controllers.configuration.DashboardOrganizationAPI;
import com.constructiveactivists.dashboardsandreportsmodule.controllers.response.CardsOrganizationVolunteerResponse;
import com.constructiveactivists.dashboardsandreportsmodule.services.DashboardOrganizationService;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ReviewEntity;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;
import java.util.List;
import java.util.Map;


@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.organization-dashboard}")
public class DashboardOrganizationController implements DashboardOrganizationAPI {

    private final DashboardOrganizationService dashboardOrganizationService;

    @Override
    public ResponseEntity<Long> getActiveOrganizationsCount() {
        long count = dashboardOrganizationService.getActiveOrganizationCount();
        return ResponseEntity.ok(count);
    }

    @Override
    public ResponseEntity<Long>getCompleteMissionsCount() {
        long completeMissionsCount = dashboardOrganizationService.countCompleteMissions();
        return ResponseEntity.ok(completeMissionsCount);
    }

    @Override
    public ResponseEntity<Double> getAverageHoursForOrganization(Integer organizationId) {
        Double averageHours = dashboardOrganizationService.getAverageHoursForOrganization(organizationId);
        return ResponseEntity.ok(averageHours);
    }

    @Override
    public ResponseEntity<Double> getAverageMonthlyHoursByOrganization(Integer organizationId) {
        Double averageMonthlyHours = dashboardOrganizationService.getAverageMonthlyHoursByOrganization(organizationId);
        return ResponseEntity.ok(averageMonthlyHours);
    }

    @Override
    public ResponseEntity<List<VolunteerOrganizationEntity>> getVolunteersByOrganizationAndMonth(Integer organizationId, Integer month, Integer year) {
        List<VolunteerOrganizationEntity> volunteerOrganizations = dashboardOrganizationService.getVolunteersByOrganizationAndMonth(organizationId, month, year);
        return ResponseEntity.ok(volunteerOrganizations);
    }

    @Override
    public ResponseEntity<List<OrganizationEntity>> getTenRecentOrganizations() {
        try {
            List<OrganizationEntity> recentOrganizations = dashboardOrganizationService.getTenRecentOrganizations();
            return ResponseEntity.ok(recentOrganizations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Map<Month, Long>> getOrganizationsCountByMonth(@PathVariable int year) {
        Map<Month, Long> organizationsByMonth = dashboardOrganizationService.getOrganizationsCountByMonth(year);
        return ResponseEntity.ok(organizationsByMonth);
    }

    @Override
    public ResponseEntity<Long> getCompletedMissionsCountByOrganization(@PathVariable Integer organizationId) {
        long completedMissionsCount = dashboardOrganizationService.countCompleteMissionsByOrganization(organizationId);
        return ResponseEntity.ok(completedMissionsCount);
    }

    @Override
    public ResponseEntity<Map<SkillEnum, Integer>> getSkillCountsByOrganization(@PathVariable Integer organizationId) {
        Map<SkillEnum, Integer> skillCounts = dashboardOrganizationService.getSkillCountsByOrganization(organizationId);
        return ResponseEntity.ok(skillCounts);
    }

    @Override
    public ResponseEntity<Map<AvailabilityEnum, Long>> getVolunteerAvailabilityCountByOrganization(
            @PathVariable Integer organizationId) {
        Map<AvailabilityEnum, Long> availabilityCountMap =
                dashboardOrganizationService.getVolunteerAvailabilityCountByOrganization(organizationId);
        return ResponseEntity.ok(availabilityCountMap);
    }

    @Override
    public ResponseEntity<Integer> getTotalBeneficiariesImpactedByOrganization(@PathVariable Integer organizationId) {
        int totalBeneficiaries = dashboardOrganizationService.getTotalBeneficiariesImpactedByOrganization(organizationId);
        return ResponseEntity.ok(totalBeneficiaries);
    }

    @Override
    public ResponseEntity<List<ReviewEntity>> getHistoryByOrganization(@PathVariable Integer organizationId) {
        List<ReviewEntity> reviews = dashboardOrganizationService.getReviewsByOrganization(organizationId);
        return ResponseEntity.ok(reviews);
    }

    @Override
    public ResponseEntity<List<ReviewEntity>> getCoordinatorReviewHistory(@PathVariable Integer userId) {
        List<ReviewEntity> reviews = dashboardOrganizationService.getCoordinatorReviewHistory(userId);
        return ResponseEntity.ok(reviews);
    }

    @Override
    public ResponseEntity<Map<String, Long>> getActivitiesCountByOrganizationAndYear(
            @RequestParam Integer organizationId,
            @RequestParam int year) {
        Map<String, Long> activitiesCount = dashboardOrganizationService.getActivitiesCountByOrganizationAndYear(organizationId, year);
        return new ResponseEntity<>(activitiesCount, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CardsOrganizationVolunteerResponse>> getAllOrganizations() {
        List<CardsOrganizationVolunteerResponse> organizations = dashboardOrganizationService.getAllOrganizationsCards();
        return ResponseEntity.ok(organizations);
    }

}
