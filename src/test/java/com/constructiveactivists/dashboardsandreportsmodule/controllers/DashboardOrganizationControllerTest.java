package com.constructiveactivists.dashboardsandreportsmodule.controllers;

import com.constructiveactivists.dashboardsandreportsmodule.services.DashboardOrganizationService;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardOrganizationControllerTest {

    @InjectMocks
    private DashboardOrganizationController dashboardOrganizationController;

    @Mock
    private DashboardOrganizationService dashboardOrganizationService;

    @Test
    void testGetActiveOrganizationsCount() {
        long expectedCount = 10L;
        when(dashboardOrganizationService.getActiveOrganizationCount()).thenReturn(expectedCount);

        ResponseEntity<Long> response = dashboardOrganizationController.getActiveOrganizationsCount();

        assertNotNull(response);
        assertEquals(expectedCount, response.getBody());
        verify(dashboardOrganizationService, times(1)).getActiveOrganizationCount();
    }

    @Test
    void testGetCompleteMissionsCount() {
        long expectedCount = 5L;
        when(dashboardOrganizationService.countCompleteMissions()).thenReturn(expectedCount);

        ResponseEntity<Long> response = dashboardOrganizationController.getCompleteMissionsCount();

        assertNotNull(response);
        assertEquals(expectedCount, response.getBody());
        verify(dashboardOrganizationService, times(1)).countCompleteMissions();
    }

    @Test
    void testGetAverageHoursForOrganization() {
        int organizationId = 1;
        double expectedAverageHours = 25.0;
        when(dashboardOrganizationService.getAverageHoursForOrganization(organizationId)).thenReturn(expectedAverageHours);

        ResponseEntity<Double> response = dashboardOrganizationController.getAverageHoursForOrganization(organizationId);

        assertNotNull(response);
        assertEquals(expectedAverageHours, response.getBody());
        verify(dashboardOrganizationService, times(1)).getAverageHoursForOrganization(organizationId);
    }

    @Test
    void testGetAverageMonthlyHoursByOrganization() {
        int organizationId = 1;
        double expectedAverageMonthlyHours = 20.0;
        when(dashboardOrganizationService.getAverageMonthlyHoursByOrganization(organizationId)).thenReturn(expectedAverageMonthlyHours);

        ResponseEntity<Double> response = dashboardOrganizationController.getAverageMonthlyHoursByOrganization(organizationId);

        assertNotNull(response);
        assertEquals(expectedAverageMonthlyHours, response.getBody());
        verify(dashboardOrganizationService, times(1)).getAverageMonthlyHoursByOrganization(organizationId);
    }

    @Test
    void testGetVolunteersByOrganizationAndMonth() {
        int organizationId = 1;
        int month = 5;
        int year = 2024;
        List<VolunteerOrganizationEntity> expectedVolunteers = List.of(new VolunteerOrganizationEntity());
        when(dashboardOrganizationService.getVolunteersByOrganizationAndMonth(organizationId, month, year))
                .thenReturn(expectedVolunteers);

        ResponseEntity<List<VolunteerOrganizationEntity>> response = dashboardOrganizationController.getVolunteersByOrganizationAndMonth(organizationId, month, year);

        assertNotNull(response);
        assertEquals(expectedVolunteers, response.getBody());
        verify(dashboardOrganizationService, times(1)).getVolunteersByOrganizationAndMonth(organizationId, month, year);
    }


}
