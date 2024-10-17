package com.constructiveactivists.dashboardsandreportsmodule.services;

import com.constructiveactivists.dashboardsandreportsmodule.controllers.response.CardsOrganizationVolunteerResponse;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
}
