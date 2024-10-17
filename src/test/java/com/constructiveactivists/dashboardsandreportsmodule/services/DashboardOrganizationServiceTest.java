package com.constructiveactivists.dashboardsandreportsmodule.services;

import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymodule.services.mission.MissionService;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.DataShareVolunteerOrganizationService;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.VolunteerOrganizationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.NOT_FOUND_MESSAGE;
import static com.constructiveactivists.configurationmodule.constants.AppConstants.ORGANIZATION_MESSAGE_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardOrganizationServiceTest {

    @InjectMocks
    private DashboardOrganizationService dashboardOrganizationService;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private UserService userService;

    @Mock
    private MissionService missionService;

    @Mock
    private PostulationService postulationService;

    @Mock
    private VolunteerOrganizationService volunteerOrganizationService;

    @Mock
    private DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;

    private OrganizationEntity organization;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        organization = new OrganizationEntity();
        organization.setUserId(1);

        user = new UserEntity();
        user.setAuthorizationType(AuthorizationStatus.AUTORIZADO);
    }

    @Test
    void testGetActiveOrganizationCount() {
        when(organizationService.getAllOrganizations()).thenReturn(List.of(organization));
        when(userService.getUserById(1)).thenReturn(Optional.of(user));

        long count = dashboardOrganizationService.getActiveOrganizationCount();

        assertEquals(1, count);
        verify(organizationService, times(1)).getAllOrganizations();
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void testCountCompleteMissions() {

        MissionEntity missionCompleted = mock(MissionEntity.class);
        MissionEntity missionAvailable = mock(MissionEntity.class);

        when(missionCompleted.getMissionStatus()).thenReturn(MissionStatusEnum.COMPLETADA);
        when(missionAvailable.getMissionStatus()).thenReturn(MissionStatusEnum.DISPONIBLE);

        when(missionService.getAllMisions()).thenReturn(List.of(missionCompleted, missionAvailable));

        long count = dashboardOrganizationService.countCompleteMissions();

        assertEquals(1, count);

        verify(missionService, times(1)).getAllMisions();
    }

    @Test
    void testGetAverageHoursForOrganization() {
        Integer organizationId = 1;
        List<DataShareVolunteerOrganizationEntity> dataShares = List.of(
                createDataShare(10, 5),
                createDataShare(8, 3)
        );
        when(organizationService.getOrganizationById(organizationId)).thenReturn(Optional.of(organization));
        when(volunteerOrganizationService.findVolunteerOrganizationIdsByOrganizationId(organizationId))
                .thenReturn(List.of(1, 2));
        when(dataShareVolunteerOrganizationService.findAllByVolunteerOrganizationIdIn(anyList())).thenReturn(dataShares);

        double avgHours = dashboardOrganizationService.getAverageHoursForOrganization(organizationId);

        assertEquals(5.0, avgHours);
        verify(organizationService, times(1)).getOrganizationById(organizationId);
        verify(volunteerOrganizationService, times(1)).findVolunteerOrganizationIdsByOrganizationId(organizationId);
        verify(dataShareVolunteerOrganizationService, times(1)).findAllByVolunteerOrganizationIdIn(anyList());
    }

    private DataShareVolunteerOrganizationEntity createDataShare(Integer hoursDone, Integer hoursCertified) {
        DataShareVolunteerOrganizationEntity dataShare = new DataShareVolunteerOrganizationEntity();
        dataShare.setHoursDone(hoursDone);
        dataShare.setHoursCertified(hoursCertified);
        return dataShare;
    }

    @Test
    void testGetAverageMonthlyHoursByOrganization() {
        Integer organizationId = 1;
        List<DataShareVolunteerOrganizationEntity> dataShares = List.of(
                createDataShareWithMonthlyHours(10),
                createDataShareWithMonthlyHours(20)
        );
        when(organizationService.getOrganizationById(organizationId)).thenReturn(Optional.of(organization));
        when(volunteerOrganizationService.findVolunteerOrganizationIdsByOrganizationId(organizationId))
                .thenReturn(List.of(1, 2));
        when(dataShareVolunteerOrganizationService.findAllByVolunteerOrganizationIdIn(anyList())).thenReturn(dataShares);

        double avgMonthlyHours = dashboardOrganizationService.getAverageMonthlyHoursByOrganization(organizationId);

        assertEquals(15.0, avgMonthlyHours);
        verify(organizationService, times(1)).getOrganizationById(organizationId);
        verify(volunteerOrganizationService, times(1)).findVolunteerOrganizationIdsByOrganizationId(organizationId);
        verify(dataShareVolunteerOrganizationService, times(1)).findAllByVolunteerOrganizationIdIn(anyList());
    }

    private DataShareVolunteerOrganizationEntity createDataShareWithMonthlyHours(Integer monthlyHours) {
        DataShareVolunteerOrganizationEntity dataShare = new DataShareVolunteerOrganizationEntity();
        dataShare.setMonthlyHours(monthlyHours);
        return dataShare;
    }

    @Test
    void testGetVolunteersByOrganizationAndMonth() {
        Integer organizationId = 1;
        Integer month = 5;
        Integer year = 2023;

        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();
        volunteerOrganization.setId(1);

        PostulationEntity postulation = new PostulationEntity();
        postulation.setVolunteerOrganizationId(1);

        when(organizationService.getOrganizationById(organizationId)).thenReturn(Optional.of(organization));
        when(volunteerOrganizationService.getVolunteersByOrganizationId(organizationId)).thenReturn(List.of(volunteerOrganization));
        when(postulationService.getPostulationsByDateRange(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(postulation));

        List<VolunteerOrganizationEntity> result = dashboardOrganizationService.getVolunteersByOrganizationAndMonth(organizationId, month, year);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(volunteerOrganization.getId(), result.get(0).getId());

        verify(organizationService, times(1)).getOrganizationById(organizationId);
        verify(volunteerOrganizationService, times(1)).getVolunteersByOrganizationId(organizationId);
        verify(postulationService, times(1)).getPostulationsByDateRange(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void testGetOrganizationById_ThrowsEntityNotFoundException() {
        Integer organizationId = 1;
        when(organizationService.getOrganizationById(organizationId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> dashboardOrganizationService.getAverageHoursForOrganization(organizationId));

        assertEquals(ORGANIZATION_MESSAGE_ID + organizationId + NOT_FOUND_MESSAGE, exception.getMessage());
        verify(organizationService, times(1)).getOrganizationById(organizationId);
    }
}
