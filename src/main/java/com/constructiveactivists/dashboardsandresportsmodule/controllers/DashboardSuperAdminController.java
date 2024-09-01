package com.constructiveactivists.dashboardsandresportsmodule.controllers;

import com.constructiveactivists.dashboardsandresportsmodule.controllers.configuration.DashboardSuperAdminAPI;
import com.constructiveactivists.dashboardsandresportsmodule.services.DashboardSuperAdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.super-admin-dashboard}")
public class DashboardSuperAdminController implements DashboardSuperAdminAPI {

    private final DashboardSuperAdminService dashboardSuperAdminService;

    @Override
    public ResponseEntity<Map<String, Long>> getActivityLocalitiesWithFrequency() {
        Map<String, Long> localityFrequencyMap = dashboardSuperAdminService.getActivityLocalitiesWithFrequency();
        if (localityFrequencyMap.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(localityFrequencyMap, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Double> getAverageHoursAllVolunteers() {
        double averageHours = dashboardSuperAdminService.getAverageHours();
        return ResponseEntity.ok(averageHours);
    }

    @Override
    public ResponseEntity<Double> getAverageMonthlyHoursAllVolunteers() {
        Double averageMonthlyHours = dashboardSuperAdminService.getAverageMonthlyHours();
        return ResponseEntity.ok(averageMonthlyHours);
    }
}
