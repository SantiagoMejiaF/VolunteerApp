package com.constructiveactivists.resportsanddashboardsmodule.controllers;

import com.constructiveactivists.resportsanddashboardsmodule.controllers.configuration.DashboardOrganizationAPI;
import com.constructiveactivists.resportsanddashboardsmodule.services.DashboardOrganizationService;
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
}
