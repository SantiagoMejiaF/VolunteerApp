package com.constructiveactivists.dashboardsandreportsmodule.controllers;

import com.constructiveactivists.dashboardsandreportsmodule.controllers.response.CardsOrganizationVolunteerResponse;
import com.constructiveactivists.dashboardsandreportsmodule.services.DashboardOrganizationService;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ReviewEntity;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    void testGetTenRecentOrganizations_Success() {
        OrganizationEntity org1 = new OrganizationEntity();
        org1.setId(1);

        OrganizationEntity org2 = new OrganizationEntity();
        org2.setId(2);

        List<OrganizationEntity> recentOrganizations = List.of(org1, org2);

        when(dashboardOrganizationService.getTenRecentOrganizations()).thenReturn(recentOrganizations);

        ResponseEntity<List<OrganizationEntity>> response = dashboardOrganizationController.getTenRecentOrganizations();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(recentOrganizations, response.getBody());
        verify(dashboardOrganizationService, times(1)).getTenRecentOrganizations();
    }

    @Test
    void testGetTenRecentOrganizations_Error() {
        when(dashboardOrganizationService.getTenRecentOrganizations()).thenThrow(new RuntimeException("Error fetching organizations"));
        ResponseEntity<List<OrganizationEntity>> response = dashboardOrganizationController.getTenRecentOrganizations();
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(dashboardOrganizationService, times(1)).getTenRecentOrganizations();
    }

    @Test
    void testGetOrganizationsCountByMonth_Success() {
        int year = 2024;

        Map<Month, Long> expectedCounts = Map.of(
                Month.JANUARY, 5L,
                Month.FEBRUARY, 10L,
                Month.MARCH, 15L
        );

        when(dashboardOrganizationService.getOrganizationsCountByMonth(year)).thenReturn(expectedCounts);

        ResponseEntity<Map<Month, Long>> response = dashboardOrganizationController.getOrganizationsCountByMonth(year);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCounts, response.getBody());
        verify(dashboardOrganizationService, times(1)).getOrganizationsCountByMonth(year);
    }

    @Test
    void testGetOrganizationsCountByMonth_NoData() {
        int year = 2024;

        Map<Month, Long> expectedCounts = new HashMap<>();
        when(dashboardOrganizationService.getOrganizationsCountByMonth(year)).thenReturn(expectedCounts);
        ResponseEntity<Map<Month, Long>> response = dashboardOrganizationController.getOrganizationsCountByMonth(year);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCounts, response.getBody());
        verify(dashboardOrganizationService, times(1)).getOrganizationsCountByMonth(year);
    }

    @Test
    void testGetCompletedMissionsCountByOrganization_Success() {
        int organizationId = 1;
        long expectedCount = 5L;

        when(dashboardOrganizationService.countCompleteMissionsByOrganization(organizationId)).thenReturn(expectedCount);

        ResponseEntity<Long> response = dashboardOrganizationController.getCompletedMissionsCountByOrganization(organizationId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCount, response.getBody());
        verify(dashboardOrganizationService, times(1)).countCompleteMissionsByOrganization(organizationId);
    }

    @Test
    void testGetSkillCountsByOrganization_Success() {
        int organizationId = 1;

        Map<SkillEnum, Integer> expectedSkillCounts = Map.of(SkillEnum.COMUNICACION, 5, SkillEnum.LIDERAZGO, 3);

        when(dashboardOrganizationService.getSkillCountsByOrganization(organizationId)).thenReturn(expectedSkillCounts);

        ResponseEntity<Map<SkillEnum, Integer>> response = dashboardOrganizationController.getSkillCountsByOrganization(organizationId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSkillCounts, response.getBody());
        verify(dashboardOrganizationService, times(1)).getSkillCountsByOrganization(organizationId);
    }

    @Test
    void testGetTotalBeneficiariesImpactedByOrganization_Success() {
        int organizationId = 1;
        int expectedTotalBeneficiaries = 100;

        when(dashboardOrganizationService.getTotalBeneficiariesImpactedByOrganization(organizationId))
                .thenReturn(expectedTotalBeneficiaries);

        ResponseEntity<Integer> response = dashboardOrganizationController.getTotalBeneficiariesImpactedByOrganization(organizationId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTotalBeneficiaries, response.getBody());
        verify(dashboardOrganizationService, times(1)).getTotalBeneficiariesImpactedByOrganization(organizationId);
    }

    @Test
    void testGetVolunteerAvailabilityCountByOrganization() {
        int organizationId = 1;
        Map<AvailabilityEnum, Long> expectedAvailabilityCount = Map.of(
                AvailabilityEnum.LUNES, 5L,
                AvailabilityEnum.MARTES, 10L
        );

        when(dashboardOrganizationService.getVolunteerAvailabilityCountByOrganization(organizationId))
                .thenReturn(expectedAvailabilityCount);

        ResponseEntity<Map<AvailabilityEnum, Long>> response =
                dashboardOrganizationController.getVolunteerAvailabilityCountByOrganization(organizationId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAvailabilityCount, response.getBody());
        verify(dashboardOrganizationService, times(1)).getVolunteerAvailabilityCountByOrganization(organizationId);
    }

    @Test
    void testGetHistoryByOrganization() {
        int organizationId = 1;
        List<ReviewEntity> expectedReviews = List.of(new ReviewEntity());

        when(dashboardOrganizationService.getReviewsByOrganization(organizationId)).thenReturn(expectedReviews);

        ResponseEntity<List<ReviewEntity>> response = dashboardOrganizationController.getHistoryByOrganization(organizationId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedReviews, response.getBody());
        verify(dashboardOrganizationService, times(1)).getReviewsByOrganization(organizationId);
    }

    @Test
    void testGetCoordinatorReviewHistory() {
        int userId = 1;
        List<ReviewEntity> expectedReviews = List.of(new ReviewEntity());

        when(dashboardOrganizationService.getCoordinatorReviewHistory(userId)).thenReturn(expectedReviews);

        ResponseEntity<List<ReviewEntity>> response = dashboardOrganizationController.getCoordinatorReviewHistory(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedReviews, response.getBody());
        verify(dashboardOrganizationService, times(1)).getCoordinatorReviewHistory(userId);
    }

    @Test
    void testGetActivitiesCountByOrganizationAndYear() {
        int organizationId = 1;
        int year = 2024;
        Map<String, Long> expectedActivitiesCount = Map.of(
                "January", 5L,
                "February", 3L
        );

        when(dashboardOrganizationService.getActivitiesCountByOrganizationAndYear(organizationId, year))
                .thenReturn(expectedActivitiesCount);

        ResponseEntity<Map<String, Long>> response = dashboardOrganizationController.getActivitiesCountByOrganizationAndYear(organizationId, year);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedActivitiesCount, response.getBody());
        verify(dashboardOrganizationService, times(1)).getActivitiesCountByOrganizationAndYear(organizationId, year);
    }

    @Test
    void testGetAllOrganizations() {
        List<CardsOrganizationVolunteerResponse> expectedOrganizations = List.of(new CardsOrganizationVolunteerResponse());

        when(dashboardOrganizationService.getAllOrganizationsCards()).thenReturn(expectedOrganizations);

        ResponseEntity<List<CardsOrganizationVolunteerResponse>> response = dashboardOrganizationController.getAllOrganizations();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOrganizations, response.getBody());
        verify(dashboardOrganizationService, times(1)).getAllOrganizationsCards();
    }
}
