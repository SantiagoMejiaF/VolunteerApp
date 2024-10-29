package com.constructiveactivists.dashboardsandreportsmodule.controllers;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.dashboardsandreportsmodule.controllers.response.CardsOrganizationVolunteerResponse;
import com.constructiveactivists.dashboardsandreportsmodule.services.DashboardVolunteerService;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Month;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardVolunteerControllerTest {

    @Mock
    private DashboardVolunteerService dashboardService;

    @InjectMocks
    private DashboardVolunteerController dashboardVolunteerController;

    @Test
    void testGetAgeRanges() {
        Map<String, Long> expectedAgeRanges = Map.of("18-25", 100L, "26-35", 200L);
        when(dashboardService.getAgeRanges()).thenReturn(expectedAgeRanges);

        ResponseEntity<Map<String, Long>> response = dashboardVolunteerController.getAgeRanges();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAgeRanges, response.getBody());
        verify(dashboardService, times(1)).getAgeRanges();
    }

    @Test
    void testGetAverageAge() {
        double expectedAverageAge = 30.5;
        when(dashboardService.getAverageAge()).thenReturn(expectedAverageAge);

        ResponseEntity<Double> response = dashboardVolunteerController.getAverageAge();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAverageAge, response.getBody());
        verify(dashboardService, times(1)).getAverageAge();
    }

    @Test
    void testGetActiveVolunteerCount() {
        long expectedCount = 500L;
        when(dashboardService.getActiveVolunteerCount()).thenReturn(expectedCount);

        ResponseEntity<Long> response = dashboardVolunteerController.getActiveVolunteerCount();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCount, response.getBody());
        verify(dashboardService, times(1)).getActiveVolunteerCount();
    }

    @Test
    void testGetSkillCounts() {
        Map<SkillEnum, Integer> expectedSkillCounts = Map.of(SkillEnum.COMUNICACION, 10, SkillEnum.EDUCACION, 20);
        when(dashboardService.getSkillCounts()).thenReturn(expectedSkillCounts);

        ResponseEntity<Map<SkillEnum, Integer>> response = dashboardVolunteerController.getSkillCounts();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSkillCounts, response.getBody());
        verify(dashboardService, times(1)).getSkillCounts();
    }

    @Test
    void testGetVolunteerAvailabilityCount() {
        Map<AvailabilityEnum, Long> expectedAvailabilityCount = Map.of(AvailabilityEnum.DOMINGO, 50L, AvailabilityEnum.SABADO, 30L);
        when(dashboardService.getVolunteerAvailabilityCount()).thenReturn(expectedAvailabilityCount);

        ResponseEntity<Map<AvailabilityEnum, Long>> response = dashboardVolunteerController.getVolunteerAvailabilityCount();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAvailabilityCount, response.getBody());
        verify(dashboardService, times(1)).getVolunteerAvailabilityCount();
    }

    @Test
    void testGetInterestCount() {
        Map<InterestEnum, Long> expectedInterestCount = Map.of(InterestEnum.EDUCACION, 100L, InterestEnum.MEDIO_AMBIENTE, 150L);
        when(dashboardService.getInterestCount()).thenReturn(expectedInterestCount);

        ResponseEntity<Map<InterestEnum, Long>> response = dashboardVolunteerController.getInterestCount();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedInterestCount, response.getBody());
        verify(dashboardService, times(1)).getInterestCount();
    }

    @Test
    void testGetNextActivityForVolunteer_Found() {
        int volunteerId = 1;
        ActivityEntity expectedActivity = new ActivityEntity();
        when(dashboardService.getNextActivityForVolunteer(volunteerId)).thenReturn(expectedActivity);

        ResponseEntity<ActivityEntity> response = dashboardVolunteerController.getNextActivityForVolunteer(volunteerId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedActivity, response.getBody());
        verify(dashboardService, times(1)).getNextActivityForVolunteer(volunteerId);
    }

    @Test
    void testGetNextActivityForVolunteer_NotFound() {
        int volunteerId = 1;
        when(dashboardService.getNextActivityForVolunteer(volunteerId)).thenReturn(null);

        ResponseEntity<ActivityEntity> response = dashboardVolunteerController.getNextActivityForVolunteer(volunteerId);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(dashboardService, times(1)).getNextActivityForVolunteer(volunteerId);
    }

    @Test
    void testGetNextActivityForVolunteer_BusinessException() {
        int volunteerId = 1;
        when(dashboardService.getNextActivityForVolunteer(volunteerId)).thenThrow(new BusinessException("Error"));

        ResponseEntity<ActivityEntity> response = dashboardVolunteerController.getNextActivityForVolunteer(volunteerId);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(dashboardService, times(1)).getNextActivityForVolunteer(volunteerId);
    }

    @Test
    void testGetFoundationsByVolunteerId() {
        int volunteerId = 1;
        List<CardsOrganizationVolunteerResponse> expectedFoundations = List.of(new CardsOrganizationVolunteerResponse());
        when(dashboardService.getFoundationsByVolunteerId(volunteerId)).thenReturn(expectedFoundations);

        ResponseEntity<List<CardsOrganizationVolunteerResponse>> response = dashboardVolunteerController.getFoundationsByVolunteerId(volunteerId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedFoundations, response.getBody());
        verify(dashboardService, times(1)).getFoundationsByVolunteerId(volunteerId);
    }

    @Test
    void testGetTenRecentVolunteers_Success() {
        List<VolunteerEntity> expectedVolunteers = List.of(new VolunteerEntity());

        when(dashboardService.getTenRecentVolunteers()).thenReturn(expectedVolunteers);

        ResponseEntity<List<VolunteerEntity>> response = dashboardVolunteerController.getTenRecentVolunteers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedVolunteers, response.getBody());
        verify(dashboardService, times(1)).getTenRecentVolunteers();
    }

    @Test
    void testGetTenRecentVolunteers_BusinessException() {
        when(dashboardService.getTenRecentVolunteers()).thenThrow(new BusinessException("No volunteers found"));

        ResponseEntity<List<VolunteerEntity>> response = dashboardVolunteerController.getTenRecentVolunteers();

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(dashboardService, times(1)).getTenRecentVolunteers();
    }

    @Test
    void testGetVolunteersCountByMonth_Success() {
        int year = 2024;
        Map<Month, Long> expectedVolunteersCount = Map.of(
                Month.JANUARY, 5L,
                Month.FEBRUARY, 10L
        );

        when(dashboardService.getVolunteersCountByMonth(year)).thenReturn(expectedVolunteersCount);

        ResponseEntity<Map<Month, Long>> response = dashboardVolunteerController.getVolunteersCountByMonth(year);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedVolunteersCount, response.getBody());
        verify(dashboardService, times(1)).getVolunteersCountByMonth(year);
    }

    @Test
    void testGetVolunteersCountByMonth_NoData() {
        int year = 2024;
        Map<Month, Long> expectedVolunteersCount = Map.of();

        when(dashboardService.getVolunteersCountByMonth(year)).thenReturn(expectedVolunteersCount);

        ResponseEntity<Map<Month, Long>> response = dashboardVolunteerController.getVolunteersCountByMonth(year);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedVolunteersCount, response.getBody());
        verify(dashboardService, times(1)).getVolunteersCountByMonth(year);
    }
}
