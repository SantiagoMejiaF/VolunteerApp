package com.constructiveactivists.dashboardsandreportsmodule.services;

import com.constructiveactivists.dashboardsandreportsmodule.controllers.response.CardsOrganizationVolunteerResponse;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupEntity;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupService;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.repositories.OrganizationRepository;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.entities.volunteer.PersonalInformationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteeringInformationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.services.volunteer.VolunteerService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.VolunteerOrganizationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardVolunteerServiceTest {

    @InjectMocks
    private DashboardVolunteerService dashboardVolunteerService;

    @Mock
    private VolunteerService volunteerService;

    @Mock
    private UserService userService;

    @Mock
    private VolunteerOrganizationService volunteerOrganizationService;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private PostulationService postulationService;

    @Mock
    private VolunteerGroupService volunteerGroupService;

    @Mock
    private ActivityService activityService;

    private VolunteerEntity volunteer;
    private VolunteerOrganizationEntity volunteerOrganization;

    @BeforeEach
    void setUp() {
        volunteer = new VolunteerEntity();
        volunteer.setUserId(1);

        volunteerOrganization = new VolunteerOrganizationEntity();
        volunteerOrganization.setId(1);
        volunteerOrganization.setOrganizationId(1);
    }


    @Test
    void testGetActiveVolunteerCount() {
        when(volunteerService.getAllVolunteers()).thenReturn(List.of(volunteer));
        UserEntity userEntity = new UserEntity();
        userEntity.setAuthorizationType(AuthorizationStatus.AUTORIZADO);

        when(userService.getUserById(1)).thenReturn(Optional.of(userEntity));

        long count = dashboardVolunteerService.getActiveVolunteerCount();

        assertEquals(1, count);
        verify(volunteerService, times(1)).getAllVolunteers();
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void testGetFoundationsByVolunteerId() {
        when(volunteerOrganizationService.getOrganizationsByVolunteerId(1)).thenReturn(List.of(volunteerOrganization));
        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(1);
        organization.setUserId(1);
        when(organizationRepository.findById(1)).thenReturn(Optional.of(organization));

        PostulationEntity postulation = new PostulationEntity();
        postulation.setStatus(OrganizationStatusEnum.ACEPTADO);
        when(postulationService.findById(1)).thenReturn(postulation);

        UserEntity userEntity = new UserEntity();
        userEntity.setImage("photo_url");
        when(userService.getUserById(1)).thenReturn(Optional.of(userEntity));

        List<CardsOrganizationVolunteerResponse> response = dashboardVolunteerService.getFoundationsByVolunteerId(1);

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void testGetFoundationsByVolunteerId_NoAcceptedPostulation() {
        when(volunteerOrganizationService.getOrganizationsByVolunteerId(1)).thenReturn(List.of(volunteerOrganization));
        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(1);
        organization.setUserId(1);
        when(organizationRepository.findById(1)).thenReturn(Optional.of(organization));

        PostulationEntity postulation = new PostulationEntity();
        postulation.setStatus(OrganizationStatusEnum.RECHAZADO);
        when(postulationService.findById(1)).thenReturn(postulation);

        List<CardsOrganizationVolunteerResponse> response = dashboardVolunteerService.getFoundationsByVolunteerId(1);

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void testGetAgeRanges() {
        VolunteerEntity volunteer1 = new VolunteerEntity();
        volunteer1.setPersonalInformation(new PersonalInformationEntity());
        volunteer1.getPersonalInformation().setBornDate(LocalDate.of(1990, 1, 1));

        VolunteerEntity volunteer2 = new VolunteerEntity();
        volunteer2.setPersonalInformation(new PersonalInformationEntity());
        volunteer2.getPersonalInformation().setBornDate(LocalDate.of(1980, 1, 1));

        when(volunteerService.getAllVolunteers()).thenReturn(List.of(volunteer1, volunteer2));
        when(volunteerService.calculateAge(any(LocalDate.class)))
                .thenAnswer(invocation -> Period.between((LocalDate) invocation.getArgument(0), LocalDate.now()).getYears());

        Map<String, Long> result = dashboardVolunteerService.getAgeRanges();

        assertEquals(1, result.get("30-39"));
        assertEquals(1, result.get("40-49"));
        verify(volunteerService, times(2)).calculateAge(any(LocalDate.class));
    }

    @Test
    void testGetAverageAge() {
        VolunteerEntity volunteer1 = new VolunteerEntity();
        volunteer1.setPersonalInformation(new PersonalInformationEntity());
        volunteer1.getPersonalInformation().setAge(30);

        VolunteerEntity volunteer2 = new VolunteerEntity();
        volunteer2.setPersonalInformation(new PersonalInformationEntity());
        volunteer2.getPersonalInformation().setAge(50);

        when(volunteerService.getAllVolunteers()).thenReturn(List.of(volunteer1, volunteer2));

        double averageAge = dashboardVolunteerService.getAverageAge();

        assertEquals(40.0, averageAge);
        verify(volunteerService, times(1)).getAllVolunteers();
    }

    @Test
    void testGetSkillCounts() {
        VolunteerEntity volunteer1 = new VolunteerEntity();
        VolunteeringInformationEntity volunteeringInfo1 = new VolunteeringInformationEntity();
        volunteeringInfo1.setSkillsList(List.of(SkillEnum.LIDERAZGO, SkillEnum.COMUNICACION));

        volunteer1.setVolunteeringInformation(volunteeringInfo1);

        VolunteerEntity volunteer2 = new VolunteerEntity();
        VolunteeringInformationEntity volunteeringInfo2 = new VolunteeringInformationEntity();
        volunteeringInfo2.setSkillsList(List.of(SkillEnum.LIDERAZGO));

        volunteer2.setVolunteeringInformation(volunteeringInfo2);

        when(volunteerService.getAllVolunteers()).thenReturn(List.of(volunteer1, volunteer2));

        Map<SkillEnum, Integer> skillCounts = dashboardVolunteerService.getSkillCounts();

        assertEquals(2, skillCounts.get(SkillEnum.LIDERAZGO));
        assertEquals(1, skillCounts.get(SkillEnum.COMUNICACION));
        verify(volunteerService, times(1)).getAllVolunteers();
    }

    @Test
    void testGetVolunteerAvailabilityCount() {
        VolunteerEntity volunteer1 = new VolunteerEntity();
        VolunteeringInformationEntity volunteeringInfo1 = new VolunteeringInformationEntity();
        volunteeringInfo1.setAvailabilityDaysList(List.of(AvailabilityEnum.LUNES, AvailabilityEnum.MARTES));

        volunteer1.setVolunteeringInformation(volunteeringInfo1);

        VolunteerEntity volunteer2 = new VolunteerEntity();
        VolunteeringInformationEntity volunteeringInfo2 = new VolunteeringInformationEntity();
        volunteeringInfo2.setAvailabilityDaysList(List.of(AvailabilityEnum.LUNES));

        volunteer2.setVolunteeringInformation(volunteeringInfo2);

        when(volunteerService.getAllVolunteers()).thenReturn(List.of(volunteer1, volunteer2));

        Map<AvailabilityEnum, Long> availabilityCounts = dashboardVolunteerService.getVolunteerAvailabilityCount();

        assertEquals(2, availabilityCounts.get(AvailabilityEnum.LUNES));
        assertEquals(1, availabilityCounts.get(AvailabilityEnum.MARTES));
        verify(volunteerService, times(1)).getAllVolunteers();
    }

    @Test
    void testGetInterestCount() {
        VolunteerEntity volunteer1 = new VolunteerEntity();
        VolunteeringInformationEntity volunteeringInfo1 = new VolunteeringInformationEntity();
        volunteeringInfo1.setInterestsList(List.of(InterestEnum.EDUCACION, InterestEnum.MEDIO_AMBIENTE));

        volunteer1.setVolunteeringInformation(volunteeringInfo1);

        VolunteerEntity volunteer2 = new VolunteerEntity();
        VolunteeringInformationEntity volunteeringInfo2 = new VolunteeringInformationEntity();
        volunteeringInfo2.setInterestsList(List.of(InterestEnum.EDUCACION));

        volunteer2.setVolunteeringInformation(volunteeringInfo2);

        when(volunteerService.getAllVolunteers()).thenReturn(List.of(volunteer1, volunteer2));

        Map<InterestEnum, Long> interestCounts = dashboardVolunteerService.getInterestCount();

        assertEquals(2, interestCounts.get(InterestEnum.EDUCACION));
        assertEquals(1, interestCounts.get(InterestEnum.MEDIO_AMBIENTE));
        verify(volunteerService, times(1)).getAllVolunteers();
    }

    @Test
    void testGetTenRecentVolunteers() {
        List<VolunteerEntity> volunteers = new ArrayList<>();

        VolunteerEntity volunteer1 = mock(VolunteerEntity.class);
        VolunteeringInformationEntity volunteeringInfo1 = mock(VolunteeringInformationEntity.class);
        when(volunteeringInfo1.getRegistrationDate()).thenReturn(LocalDate.of(2024, 10, 20).atStartOfDay());
        when(volunteer1.getVolunteeringInformation()).thenReturn(volunteeringInfo1);
        volunteers.add(volunteer1);

        VolunteerEntity volunteer2 = mock(VolunteerEntity.class);
        VolunteeringInformationEntity volunteeringInfo2 = mock(VolunteeringInformationEntity.class);
        when(volunteeringInfo2.getRegistrationDate()).thenReturn(LocalDate.of(2024, 10, 15).atStartOfDay());
        when(volunteer2.getVolunteeringInformation()).thenReturn(volunteeringInfo2);
        volunteers.add(volunteer2);

        for (int i = 3; i <= 15; i++) {
            VolunteerEntity volunteer = mock(VolunteerEntity.class);
            VolunteeringInformationEntity volunteeringInfo = mock(VolunteeringInformationEntity.class);
            when(volunteeringInfo.getRegistrationDate()).thenReturn(LocalDate.of(2024, 10, 20 - i).atStartOfDay());
            when(volunteer.getVolunteeringInformation()).thenReturn(volunteeringInfo);
            volunteers.add(volunteer);
        }

        when(volunteerService.getAllVolunteers()).thenReturn(volunteers);

        List<VolunteerEntity> recentVolunteers = dashboardVolunteerService.getTenRecentVolunteers();

        assertEquals(10, recentVolunteers.size(), "Should return 10 recent volunteers");

        for (int i = 1; i < recentVolunteers.size(); i++) {
            LocalDate date1 = LocalDate.from(recentVolunteers.get(i - 1).getVolunteeringInformation().getRegistrationDate());
            LocalDate date2 = LocalDate.from(recentVolunteers.get(i).getVolunteeringInformation().getRegistrationDate());
            assertTrue(date1.isAfter(date2) || date1.isEqual(date2), "Volunteers should be sorted by registration date");
        }
        verify(volunteerService, times(1)).getAllVolunteers();
    }

    @Test
    void testGetVolunteersCountByMonth() {
        int year = 2024;

        List<VolunteerEntity> volunteers = List.of(
                createVolunteer(LocalDate.of(year, Month.JANUARY, 5)),
                createVolunteer(LocalDate.of(year, Month.FEBRUARY, 10)),
                createVolunteer(LocalDate.of(year, Month.MARCH, 15)),
                createVolunteer(LocalDate.of(year, Month.JANUARY, 20)),
                createVolunteer(LocalDate.of(year, Month.MARCH, 25)),
                createVolunteer(LocalDate.of(year, Month.MARCH, 30)),
                createVolunteer(LocalDate.of(year, Month.APRIL, 5))
        );

        when(volunteerService.getVolunteersByDateRange(
                LocalDateTime.of(year, Month.JANUARY, 1, 0, 0),
                LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59, 59)))
                .thenReturn(volunteers);

        Map<Month, Long> result = dashboardVolunteerService.getVolunteersCountByMonth(year);

        assertEquals(2L, result.get(Month.JANUARY), "Should be 2 volunteers in January");
        assertEquals(1L, result.get(Month.FEBRUARY), "Should be 1 volunteer in February");
        assertEquals(3L, result.get(Month.MARCH), "Should be 3 volunteers in March");
        assertEquals(1L, result.get(Month.APRIL), "Should be 1 volunteer in April");

        assertEquals(0L, result.get(Month.MAY), "Should be 0 volunteers in May");
        assertEquals(0L, result.get(Month.JUNE), "Should be 0 volunteers in June");
        assertEquals(0L, result.get(Month.JULY), "Should be 0 volunteers in July");
        assertEquals(0L, result.get(Month.AUGUST), "Should be 0 volunteers in August");
        assertEquals(0L, result.get(Month.SEPTEMBER), "Should be 0 volunteers in September");
        assertEquals(0L, result.get(Month.OCTOBER), "Should be 0 volunteers in October");
        assertEquals(0L, result.get(Month.NOVEMBER), "Should be 0 volunteers in November");
        assertEquals(0L, result.get(Month.DECEMBER), "Should be 0 volunteers in December");

        verify(volunteerService, times(1)).getVolunteersByDateRange(
                LocalDateTime.of(year, Month.JANUARY, 1, 0, 0),
                LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59, 59));
    }

    private VolunteerEntity createVolunteer(LocalDate registrationDate) {
        VolunteerEntity volunteer1 = mock(VolunteerEntity.class);
        VolunteeringInformationEntity volunteeringInfo = mock(VolunteeringInformationEntity.class);
        when(volunteeringInfo.getRegistrationDate()).thenReturn(registrationDate.atStartOfDay());
        when(volunteer1.getVolunteeringInformation()).thenReturn(volunteeringInfo);
        return volunteer1;
    }

    @Test
    void testGetNextActivityForVolunteer_VolunteerNotFound() {
        Integer volunteerId = 1;
        when(volunteerService.getVolunteerById(volunteerId)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                dashboardVolunteerService.getNextActivityForVolunteer(volunteerId));
        assertEquals("El voluntario con el ID 1 no existe en la base de datos.", exception.getMessage());
    }

    @Test
     void testGetNextActivityForVolunteer_NoOrganizations() {
        Integer volunteerId = 1;
        when(volunteerService.getVolunteerById(volunteerId)).thenReturn(Optional.of(mock(VolunteerEntity.class)));
        when(volunteerOrganizationService.getOrganizationsByVolunteerId(volunteerId)).thenReturn(List.of());
        ActivityEntity result = dashboardVolunteerService.getNextActivityForVolunteer(volunteerId);
        assertNull(result, "Should return null if no organizations are found");
    }

    @Test
     void testGetNextActivityForVolunteer_NoVolunteerGroups() {
        Integer volunteerId = 1;
        when(volunteerService.getVolunteerById(volunteerId)).thenReturn(Optional.of(mock(VolunteerEntity.class)));
        when(volunteerOrganizationService.getOrganizationsByVolunteerId(volunteerId)).thenReturn(List.of(mock(VolunteerOrganizationEntity.class)));
        when(volunteerGroupService.getVolunteerGroupByOrganizationId(anyInt())).thenReturn(List.of());
        ActivityEntity result = dashboardVolunteerService.getNextActivityForVolunteer(volunteerId);
        assertNull(result, "La organización con el ID 1 no existe en la base de datos.");
    }

    @Test
     void testGetNextActivityForVolunteer_WithNextActivity() {
        Integer volunteerId = 1;
        when(volunteerService.getVolunteerById(volunteerId)).thenReturn(Optional.of(mock(VolunteerEntity.class)));
        VolunteerOrganizationEntity volunteerOrg = mock(VolunteerOrganizationEntity.class);
        when(volunteerOrg.getOrganizationId()).thenReturn(1);
        when(volunteerOrganizationService.getOrganizationsByVolunteerId(volunteerId)).thenReturn(List.of(volunteerOrg));
        VolunteerGroupEntity volunteerGroup = mock(VolunteerGroupEntity.class);
        when(volunteerGroupService.getVolunteerGroupByOrganizationId(1)).thenReturn(List.of(volunteerGroup));
        ActivityEntity activity = mock(ActivityEntity.class);
        when(activity.getDate()).thenReturn(LocalDate.now().plusDays(1));
        when(activity.getStartTime()).thenReturn(LocalTime.of(10, 0));
        when(activityService.getById(anyInt())).thenReturn(Optional.of(activity));
        ActivityEntity result = dashboardVolunteerService.getNextActivityForVolunteer(volunteerId);
        assertNotNull(result, "Should return an activity");
        assertEquals(activity, result, "The activity returned should be the one that is in the future");
    }

    @Test
     void testGetNextActivityForVolunteer_NoFutureActivity() {
        Integer volunteerId = 1;
        when(volunteerService.getVolunteerById(volunteerId)).thenReturn(Optional.of(mock(VolunteerEntity.class)));
        VolunteerOrganizationEntity volunteerOrg = mock(VolunteerOrganizationEntity.class);
        when(volunteerOrg.getOrganizationId()).thenReturn(1);
        when(volunteerOrganizationService.getOrganizationsByVolunteerId(volunteerId)).thenReturn(List.of(volunteerOrg));
        VolunteerGroupEntity volunteerGroup = mock(VolunteerGroupEntity.class);
        when(volunteerGroupService.getVolunteerGroupByOrganizationId(1)).thenReturn(List.of(volunteerGroup));
        ActivityEntity activity = mock(ActivityEntity.class);
        when(activity.getDate()).thenReturn(LocalDate.now().minusDays(1));
        when(activity.getStartTime()).thenReturn(LocalTime.of(10, 0));
        when(activityService.getById(anyInt())).thenReturn(Optional.of(activity));
        ActivityEntity result = dashboardVolunteerService.getNextActivityForVolunteer(volunteerId);
        assertNull(result, "Should return null if there are no future activities");
    }
}
