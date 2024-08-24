package com.constructiveactivists.resportsanddashboardsmodule.controllers;

import com.constructiveactivists.resportsanddashboardsmodule.controllers.configuration.DashboardAPI;
import com.constructiveactivists.resportsanddashboardsmodule.services.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.dashboard}")
public class DashboardController implements DashboardAPI {

    private final DashboardService dashboardService;

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

}
