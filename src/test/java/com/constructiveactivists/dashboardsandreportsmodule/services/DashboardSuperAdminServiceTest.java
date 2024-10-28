package com.constructiveactivists.dashboardsandreportsmodule.services;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.repositories.UserRepository;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.DataShareVolunteerOrganizationService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class DashboardSuperAdminServiceTest {

    @InjectMocks
    private DashboardSuperAdminService dashboardSuperAdminService;

    @Mock
    private ActivityService activityService;

    @Mock
    private DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;

    @Mock
    private PostulationService postulationService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testGetActivityLocalitiesWithFrequency() {
        ActivityEntity activity1 = new ActivityEntity();
        activity1.setLocality("Locality1");

        ActivityEntity activity2 = new ActivityEntity();
        activity2.setLocality("Locality2");

        when(activityService.getAll()).thenReturn(List.of(activity1, activity2, activity1)); // Locality1 x2, Locality2 x1

        Map<String, Long> result = dashboardSuperAdminService.getActivityLocalitiesWithFrequency();

        assertNotNull(result);
        assertEquals(2L, result.get("Locality1"));
        assertEquals(1L, result.get("Locality2"));
        verify(activityService, times(1)).getAll();
    }

    @Test
    void testGetAverageHours() {
        DataShareVolunteerOrganizationEntity entity1 = new DataShareVolunteerOrganizationEntity();
        entity1.setHoursDone(10);
        entity1.setHoursCertified(5);

        DataShareVolunteerOrganizationEntity entity2 = new DataShareVolunteerOrganizationEntity();
        entity2.setHoursDone(8);
        entity2.setHoursCertified(2);

        when(dataShareVolunteerOrganizationService.findAll()).thenReturn(List.of(entity1, entity2));

        double averageHours = dashboardSuperAdminService.getAverageHours();

        assertEquals(5.5, averageHours);
        verify(dataShareVolunteerOrganizationService, times(1)).findAll();
    }

    @Test
    void testGetAverageMonthlyHours() {
        DataShareVolunteerOrganizationEntity entity1 = new DataShareVolunteerOrganizationEntity();
        entity1.setMonthlyHours(10);

        DataShareVolunteerOrganizationEntity entity2 = new DataShareVolunteerOrganizationEntity();
        entity2.setMonthlyHours(20);

        when(dataShareVolunteerOrganizationService.findAll()).thenReturn(List.of(entity1, entity2));

        Double averageMonthlyHours = dashboardSuperAdminService.getAverageMonthlyHours();

        assertNotNull(averageMonthlyHours);
        assertEquals(15.0, averageMonthlyHours);
        verify(dataShareVolunteerOrganizationService, times(1)).findAll();
    }

    @Test
    void testGetTenRecentAuthorizedUsers() {
        UserEntity user1 = new UserEntity();
        user1.setId(1);
        user1.setRegistrationDate(LocalDate.from(LocalDateTime.now().minusDays(1)));
        UserEntity user2 = new UserEntity();
        user2.setId(2);
        user2.setRegistrationDate(LocalDate.from(LocalDateTime.now().minusDays(2)));

        UserEntity user3 = new UserEntity();
        user3.setId(3);
        user3.setRegistrationDate(LocalDate.from(LocalDateTime.now().minusDays(3)));

        when(userRepository.findTop10ByAuthorizationTypeOrderByRegistrationDateDesc(AuthorizationStatus.AUTORIZADO))
                .thenReturn(List.of(user1, user2, user3));

        List<UserEntity> recentAuthorizedUsers = dashboardSuperAdminService.getTenRecentAuthorizedUsers();

        assertNotNull(recentAuthorizedUsers);
        assertEquals(3, recentAuthorizedUsers.size());
        assertEquals(user1, recentAuthorizedUsers.get(0));
        assertEquals(user2, recentAuthorizedUsers.get(1));
        assertEquals(user3, recentAuthorizedUsers.get(2));
        verify(userRepository, times(1)).findTop10ByAuthorizationTypeOrderByRegistrationDateDesc(AuthorizationStatus.AUTORIZADO);
    }

}
