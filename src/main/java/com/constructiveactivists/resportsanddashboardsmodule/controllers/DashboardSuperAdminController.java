package com.constructiveactivists.resportsanddashboardsmodule.controllers;

import com.constructiveactivists.resportsanddashboardsmodule.controllers.configuration.DashboardSuperAdminAPI;
import com.constructiveactivists.resportsanddashboardsmodule.services.DashboardSuperAdminService;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerOrganizationEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    @Override
    public ResponseEntity<List<VolunteerOrganizationEntity>> getRecentVolunteersByWeek() {
        List<VolunteerOrganizationEntity> recentVolunteersByWeek = dashboardSuperAdminService.getRecentVolunteersByWeek();
        if (recentVolunteersByWeek.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(recentVolunteersByWeek, HttpStatus.OK);
    }


}