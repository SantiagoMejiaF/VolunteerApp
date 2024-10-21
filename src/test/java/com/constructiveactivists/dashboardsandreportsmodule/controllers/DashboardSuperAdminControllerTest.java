package com.constructiveactivists.dashboardsandreportsmodule.controllers;

import com.constructiveactivists.dashboardsandreportsmodule.services.DashboardSuperAdminService;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardSuperAdminControllerTest {

    @InjectMocks
    private DashboardSuperAdminController dashboardSuperAdminController;

    @Mock
    private DashboardSuperAdminService dashboardSuperAdminService;

    @Test
    void testGetActivityLocalitiesWithFrequency_Content() {
        Map<String, Long> localityFrequencyMap = Map.of("Localidad1", 5L, "Localidad2", 10L);
        when(dashboardSuperAdminService.getActivityLocalitiesWithFrequency()).thenReturn(localityFrequencyMap);

        ResponseEntity<Map<String, Long>> response = dashboardSuperAdminController.getActivityLocalitiesWithFrequency();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(localityFrequencyMap, response.getBody());
        verify(dashboardSuperAdminService, times(1)).getActivityLocalitiesWithFrequency();
    }

    @Test
    void testGetActivityLocalitiesWithFrequency_NoContent() {
        when(dashboardSuperAdminService.getActivityLocalitiesWithFrequency()).thenReturn(Map.of());

        ResponseEntity<Map<String, Long>> response = dashboardSuperAdminController.getActivityLocalitiesWithFrequency();

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(dashboardSuperAdminService, times(1)).getActivityLocalitiesWithFrequency();
    }

    @Test
    void testGetAverageHoursAllVolunteers() {
        double averageHours = 15.5;
        when(dashboardSuperAdminService.getAverageHours()).thenReturn(averageHours);

        ResponseEntity<Double> response = dashboardSuperAdminController.getAverageHoursAllVolunteers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(averageHours, response.getBody());
        verify(dashboardSuperAdminService, times(1)).getAverageHours();
    }

    @Test
    void testGetAverageMonthlyHoursAllVolunteers() {
        double averageMonthlyHours = 40.0;
        when(dashboardSuperAdminService.getAverageMonthlyHours()).thenReturn(averageMonthlyHours);

        ResponseEntity<Double> response = dashboardSuperAdminController.getAverageMonthlyHoursAllVolunteers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(averageMonthlyHours, response.getBody());
        verify(dashboardSuperAdminService, times(1)).getAverageMonthlyHours();
    }



}
