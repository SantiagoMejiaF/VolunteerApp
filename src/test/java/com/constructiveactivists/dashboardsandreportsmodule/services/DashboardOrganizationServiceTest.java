package com.constructiveactivists.dashboardsandreportsmodule.services;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.enums.ActivityStatusEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymodule.services.mission.MissionService;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteeringInformationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.services.volunteer.VolunteerService;
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
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

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
    private VolunteerService volunteerService;

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

    @Test
    void testGetTenRecentOrganizations() {
        // Arrange
        List<OrganizationEntity> expectedOrganizations = Arrays.asList(
                new OrganizationEntity(), new OrganizationEntity(), new OrganizationEntity(),
                new OrganizationEntity(), new OrganizationEntity(), new OrganizationEntity(),
                new OrganizationEntity(), new OrganizationEntity(), new OrganizationEntity(),
                new OrganizationEntity()
        );
        when(organizationService.getTenRecentOrganizations()).thenReturn(expectedOrganizations);
        List<OrganizationEntity> actualOrganizations = dashboardOrganizationService.getTenRecentOrganizations();
        assertEquals(expectedOrganizations, actualOrganizations);
        verify(organizationService, times(1)).getTenRecentOrganizations(); }

    @Test
    void testGetOrganizationsCountByMonth() {
        int year = 2024;
        LocalDateTime startDateTime = LocalDateTime.of(year, Month.JANUARY, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59, 59);
        OrganizationEntity org1 = new OrganizationEntity();
        org1.setRegistrationDate(LocalDateTime.of(year, Month.JANUARY, 15, 0, 0)); // Enero
        OrganizationEntity org2 = new OrganizationEntity();
        org2.setRegistrationDate(LocalDateTime.of(year, Month.FEBRUARY, 5, 0, 0)); // Febrero
        OrganizationEntity org3 = new OrganizationEntity();
        org3.setRegistrationDate(LocalDateTime.of(year, Month.JANUARY, 20, 0, 0)); // Enero
        List<OrganizationEntity> organizations = Arrays.asList(org1, org2, org3);
        Map<Month, Long> expectedCounts = new EnumMap<>(Month.class);
        for (Month month : Month.values()) {
            expectedCounts.put(month, 0L);
        }
        expectedCounts.put(Month.JANUARY, 2L);
        expectedCounts.put(Month.FEBRUARY, 1L);
        when(organizationService.getOrganizationsByDateRange(startDateTime, endDateTime))
                .thenReturn(organizations);
        Map<Month, Long> actualCounts = dashboardOrganizationService.getOrganizationsCountByMonth(year);
        assertEquals(expectedCounts, actualCounts);
        verify(organizationService, times(1)).getOrganizationsByDateRange(startDateTime, endDateTime);
    }

    @Test
    void testCountCompleteMissionsByOrganization() {
        Integer organizationId = 1;
        MissionEntity mission1 = new MissionEntity();
        mission1.setMissionStatus(MissionStatusEnum.COMPLETADA);
        mission1.setOrganizationId(organizationId);

        MissionEntity mission2 = new MissionEntity();
        mission2.setMissionStatus(MissionStatusEnum.COMPLETADA);
        mission2.setOrganizationId(organizationId);

        MissionEntity mission3 = new MissionEntity();
        mission3.setMissionStatus(MissionStatusEnum.EN_CURSO);
        mission3.setOrganizationId(organizationId);

        MissionEntity mission4 = new MissionEntity();
        mission4.setMissionStatus(MissionStatusEnum.COMPLETADA);
        mission4.setOrganizationId(2);

        List<MissionEntity> missions = Arrays.asList(mission1, mission2, mission3, mission4);
        when(missionService.getAllMisions()).thenReturn(missions);
        long completedMissionsCount = dashboardOrganizationService.countCompleteMissionsByOrganization(organizationId);
        assertEquals(2, completedMissionsCount);
    }

    @Test
    void testGetSkillCountsByOrganization() {
        Integer organizationId = 1;
        List<SkillEnum> skillsVolunteer1 = List.of(SkillEnum.COMUNICACION, SkillEnum.TRABAJO_EN_EQUIPO);
        List<SkillEnum> skillsVolunteer2 = List.of(SkillEnum.TRABAJO_EN_EQUIPO, SkillEnum.LIDERAZGO);
        VolunteerEntity volunteer1 = new VolunteerEntity();
        volunteer1.setId(1);
        VolunteeringInformationEntity volunteeringInfo1 = new VolunteeringInformationEntity();
        volunteeringInfo1.setSkillsList(skillsVolunteer1);
        volunteer1.setVolunteeringInformation(volunteeringInfo1);
        VolunteerEntity volunteer2 = new VolunteerEntity();
        volunteer2.setId(2);
        VolunteeringInformationEntity volunteeringInfo2 = new VolunteeringInformationEntity();
        volunteeringInfo2.setSkillsList(skillsVolunteer2);
        volunteer2.setVolunteeringInformation(volunteeringInfo2);
        VolunteerOrganizationEntity volunteerOrgEntity1 = new VolunteerOrganizationEntity();
        volunteerOrgEntity1.setVolunteerId(volunteer1.getId());
        VolunteerOrganizationEntity volunteerOrgEntity2 = new VolunteerOrganizationEntity();
        volunteerOrgEntity2.setVolunteerId(volunteer2.getId());
        when(volunteerOrganizationService.findAcceptedVolunteerOrganizationsByOrganizationId(organizationId))
                .thenReturn(List.of(volunteerOrgEntity1, volunteerOrgEntity2));
        when(volunteerService.getVolunteerById(volunteerOrgEntity1.getVolunteerId()))
                .thenReturn(Optional.of(volunteer1));
        when(volunteerService.getVolunteerById(volunteerOrgEntity2.getVolunteerId()))
                .thenReturn(Optional.of(volunteer2));
        Map<SkillEnum, Integer> skillCounts = dashboardOrganizationService.getSkillCountsByOrganization(organizationId);
        assertEquals(11, skillCounts.size()); // Asegurarse que se contaron todas las habilidades únicas
        assertEquals(1, skillCounts.get(SkillEnum.COMUNICACION).intValue());
        assertEquals(2, skillCounts.get(SkillEnum.TRABAJO_EN_EQUIPO).intValue());
        assertEquals(1, skillCounts.get(SkillEnum.LIDERAZGO).intValue());
        verify(volunteerOrganizationService, times(1)).findAcceptedVolunteerOrganizationsByOrganizationId(organizationId);
        verify(volunteerService, times(1)).getVolunteerById(volunteerOrgEntity1.getVolunteerId());
        verify(volunteerService, times(1)).getVolunteerById(volunteerOrgEntity2.getVolunteerId());
    }

    @Test
    void testGetVolunteerAvailabilityCountByOrganization() {
        Integer organizationId = 1;
        VolunteerOrganizationEntity volunteerOrgEntity1 = new VolunteerOrganizationEntity();
        volunteerOrgEntity1.setVolunteerId(1);

        VolunteerOrganizationEntity volunteerOrgEntity2 = new VolunteerOrganizationEntity();
        volunteerOrgEntity2.setVolunteerId(2);

        when(volunteerOrganizationService.findAcceptedVolunteerOrganizationsByOrganizationId(organizationId))
                .thenReturn(List.of(volunteerOrgEntity1, volunteerOrgEntity2));

        VolunteerEntity volunteer1 = new VolunteerEntity();
        VolunteeringInformationEntity volunteeringInformation1 = new VolunteeringInformationEntity();
        volunteeringInformation1.setAvailabilityDaysList(List.of(AvailabilityEnum.LUNES, AvailabilityEnum.MIERCOLES));

        volunteer1.setVolunteeringInformation(volunteeringInformation1);

        VolunteerEntity volunteer2 = new VolunteerEntity();
        VolunteeringInformationEntity volunteeringInformation2 = new VolunteeringInformationEntity();
        volunteeringInformation2.setAvailabilityDaysList(List.of(AvailabilityEnum.MIERCOLES, AvailabilityEnum.VIERNES));

        volunteer2.setVolunteeringInformation(volunteeringInformation2);

        when(volunteerService.getVolunteerById(1)).thenReturn(Optional.of(volunteer1));
        when(volunteerService.getVolunteerById(2)).thenReturn(Optional.of(volunteer2));

        Map<AvailabilityEnum, Long> availabilityCount = dashboardOrganizationService.getVolunteerAvailabilityCountByOrganization(organizationId);

        Map<AvailabilityEnum, Long> expectedCounts = new EnumMap<>(AvailabilityEnum.class);
        expectedCounts.put(AvailabilityEnum.LUNES, 1L);
        expectedCounts.put(AvailabilityEnum.MIERCOLES, 2L);
        expectedCounts.put(AvailabilityEnum.VIERNES, 1L);
        for (AvailabilityEnum day : AvailabilityEnum.values()) {
            expectedCounts.putIfAbsent(day, 0L);
        }
        assertEquals(expectedCounts, availabilityCount);
        verify(volunteerOrganizationService, times(1)).findAcceptedVolunteerOrganizationsByOrganizationId(organizationId);
        verify(volunteerService, times(1)).getVolunteerById(1);
        verify(volunteerService, times(1)).getVolunteerById(2);
    }

    @Test
    void testGetTotalBeneficiariesImpactedByOrganization() {
        Integer organizationId = 1;

        MissionEntity mission1 = new MissionEntity();
        mission1.setId(1);
        MissionEntity mission2 = new MissionEntity();
        mission2.setId(2);
        when(missionService.getMissionsByOrganizationId(organizationId))
                .thenReturn(List.of(mission1, mission2));
        ActivityEntity activity1 = new ActivityEntity();
        activity1.setNumberOfBeneficiaries(10);
        activity1.setActivityStatus(ActivityStatusEnum.COMPLETADA);
        ActivityEntity activity2 = new ActivityEntity();
        activity2.setNumberOfBeneficiaries(20);
        activity2.setActivityStatus(ActivityStatusEnum.COMPLETADA);
        ActivityEntity activity3 = new ActivityEntity();
        activity3.setNumberOfBeneficiaries(10); // Duplicado
        activity3.setActivityStatus(ActivityStatusEnum.EN_CURSO);
        when(missionService.getActivitiesByMissionId(mission1.getId()))
                .thenReturn(List.of(activity1, activity3));
        when(missionService.getActivitiesByMissionId(mission2.getId()))
                .thenReturn(List.of(activity2));
        int totalBeneficiaries = dashboardOrganizationService.getTotalBeneficiariesImpactedByOrganization(organizationId);
        int expectedTotal = 30;
        assertEquals(expectedTotal, totalBeneficiaries);
        verify(missionService, times(1)).getMissionsByOrganizationId(organizationId);
        verify(missionService, times(1)).getActivitiesByMissionId(mission1.getId());
        verify(missionService, times(1)).getActivitiesByMissionId(mission2.getId());
    }




}
