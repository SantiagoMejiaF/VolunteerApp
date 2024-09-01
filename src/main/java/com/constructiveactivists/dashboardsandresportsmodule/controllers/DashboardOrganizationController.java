package com.constructiveactivists.dashboardsandresportsmodule.controllers;

import com.constructiveactivists.dashboardsandresportsmodule.controllers.configuration.DashboardOrganizationAPI;
import com.constructiveactivists.dashboardsandresportsmodule.services.DashboardOrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
