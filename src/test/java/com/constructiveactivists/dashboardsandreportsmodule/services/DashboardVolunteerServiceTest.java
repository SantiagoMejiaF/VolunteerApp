package com.constructiveactivists.dashboardsandreportsmodule.services;

import com.constructiveactivists.dashboardsandreportsmodule.controllers.response.CardsOrganizationVolunteerResponse;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.repositories.OrganizationRepository;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
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

import java.util.List;
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
        postulation.setStatus(OrganizationStatusEnum.RECHAZADO); // Postulación no aceptada
        when(postulationService.findById(1)).thenReturn(postulation);

        List<CardsOrganizationVolunteerResponse> response = dashboardVolunteerService.getFoundationsByVolunteerId(1);

        assertNotNull(response);
        assertTrue(response.isEmpty()); // Como la postulación no está aceptada, no debe haber resultados
    }
}
