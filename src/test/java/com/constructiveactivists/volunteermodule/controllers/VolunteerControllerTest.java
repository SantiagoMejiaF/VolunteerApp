package com.constructiveactivists.volunteermodule.controllers;

import com.constructiveactivists.volunteermodule.controllers.request.volunteer.VolunteerRequest;
import com.constructiveactivists.volunteermodule.controllers.request.volunteer.VolunteerUpdateRequest;
import com.constructiveactivists.volunteermodule.controllers.response.RankedOrganizationResponse;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.RelationshipEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import com.constructiveactivists.volunteermodule.mappers.volunteer.RankedOrganizationMapper;
import com.constructiveactivists.volunteermodule.mappers.volunteer.VolunteerMapper;
import com.constructiveactivists.volunteermodule.mappers.volunteer.VolunteerUpdateMapper;
import com.constructiveactivists.volunteermodule.services.volunteer.VolunteerService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VolunteerControllerTest {

    @Mock
    private VolunteerService volunteerService;

    @Mock
    private VolunteerUpdateMapper volunteerUpdateMapper;

    @Mock
    private VolunteerMapper volunteerMapper;

    @Mock
    private RankedOrganizationMapper rankedOrganizationMapper;

    @InjectMocks
    private VolunteerController volunteerController;

    private VolunteerEntity volunteerEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        volunteerEntity = new VolunteerEntity();
        volunteerEntity.setId(1);
        volunteerEntity.setUserId(10);
    }

    @Test
    void testGetAllVolunteers() {
        List<VolunteerEntity> volunteers = List.of(volunteerEntity);
        when(volunteerService.getAllVolunteers()).thenReturn(volunteers);

        List<VolunteerEntity> result = volunteerController.getAllVolunteers();
        assertEquals(1, result.size());
        assertEquals(volunteerEntity, result.get(0));
    }

    @Test
    void testGetVolunteerById() {
        when(volunteerService.getVolunteerById(1)).thenReturn(Optional.of(volunteerEntity));

        ResponseEntity<VolunteerEntity> response = volunteerController.getVolunteerById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(volunteerEntity, response.getBody());
    }

    @Test
    void testGetVolunteerById_NotFound() {
        when(volunteerService.getVolunteerById(1)).thenReturn(Optional.empty());

        ResponseEntity<VolunteerEntity> response = volunteerController.getVolunteerById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetVolunteerByUserId() {
        when(volunteerService.getVolunteerByUserId(10)).thenReturn(Optional.of(volunteerEntity));

        ResponseEntity<VolunteerEntity> response = volunteerController.getVolunteerByUserId(10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(volunteerEntity, response.getBody());
    }

    @Test
    void testGetVolunteerByUserId_NotFound() {
        when(volunteerService.getVolunteerByUserId(10)).thenReturn(Optional.empty());

        ResponseEntity<VolunteerEntity> response = volunteerController.getVolunteerByUserId(10);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateVolunteer() {
        VolunteerRequest volunteerRequest = new VolunteerRequest();
        when(volunteerMapper.toEntity(volunteerRequest)).thenReturn(volunteerEntity);
        when(volunteerService.saveVolunteer(any())).thenReturn(volunteerEntity);

        ResponseEntity<VolunteerEntity> response = volunteerController.createVolunteer(volunteerRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(volunteerEntity, response.getBody());
    }

    @Test
    void testDeleteVolunteer() {
        doNothing().when(volunteerService).deleteVolunteer(1);

        ResponseEntity<Void> response = volunteerController.deleteVolunteer(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(volunteerService, times(1)).deleteVolunteer(1);
    }

    @Test
    void testGetAllInterests() {
        List<InterestEnum> interests = List.of(InterestEnum.EDUCACION);
        when(volunteerService.getAllInterests()).thenReturn(interests);

        ResponseEntity<List<InterestEnum>> response = volunteerController.getAllInterests();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(interests, response.getBody());
    }

    @Test
    void testGetAllSkills() {
        List<SkillEnum> skills = List.of(SkillEnum.COMUNICACION);
        when(volunteerService.getAllSkills()).thenReturn(skills);

        ResponseEntity<List<SkillEnum>> response = volunteerController.getAllSkills();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(skills, response.getBody());
    }

    @Test
    void testGetAllAvailabilities() {
        List<AvailabilityEnum> availabilities = List.of(AvailabilityEnum.LUNES);
        when(volunteerService.getAllAvailabilities()).thenReturn(availabilities);

        ResponseEntity<List<AvailabilityEnum>> response = volunteerController.getAllAvailabilities();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(availabilities, response.getBody());
    }

    @Test
    void testGetAllRelationships() {
        List<RelationshipEnum> relationships = List.of(RelationshipEnum.PADRE);
        when(volunteerService.getAllRelationships()).thenReturn(relationships);

        ResponseEntity<List<RelationshipEnum>> response = volunteerController.getAllRelationships();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(relationships, response.getBody());
    }

    @Test
    void testUpdateVolunteer() {
        VolunteerUpdateRequest updateRequest = new VolunteerUpdateRequest();
        when(volunteerUpdateMapper.toEntity(updateRequest)).thenReturn(volunteerEntity);
        when(volunteerService.updateVolunteer(1, volunteerEntity)).thenReturn(volunteerEntity);

        ResponseEntity<VolunteerEntity> response = volunteerController.updateVolunteer(1, updateRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(volunteerEntity, response.getBody());
    }

    @Test
    void testPromoteVolunteerToLeader() {
        when(volunteerService.promoteToLeader(1)).thenReturn(volunteerEntity);

        ResponseEntity<VolunteerEntity> response = volunteerController.promoteVolunteerToLeader(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(volunteerEntity, response.getBody());
    }

    @Test
    void testSignUpForActivity() {
        doNothing().when(volunteerService).signUpForActivity(1, 100);

        ResponseEntity<String> response = volunteerController.signUpForActivity(1, 100);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(volunteerService, times(1)).signUpForActivity(1, 100);
    }

    @Test
    void testMatchVolunteerWithOrganizations_VolunteerNotFound() {
        Integer volunteerId = 999;
        int numberOfMatches = 5;

        when(volunteerService.matchVolunteerWithMissions(volunteerId, numberOfMatches))
                .thenThrow(new EntityNotFoundException("El voluntario con ID " + volunteerId + " no existe."));

        ResponseEntity<List<RankedOrganizationResponse>> response = volunteerController.matchVolunteerWithOrganizations(volunteerId, numberOfMatches);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(volunteerService, times(1)).matchVolunteerWithMissions(volunteerId, numberOfMatches);
        verify(rankedOrganizationMapper, never()).toResponses(anyList());
    }

    @Test
    void testMatchVolunteerWithOrganizations_InternalServerError() {
        Integer volunteerId = 1;
        int numberOfMatches = 5;

        when(volunteerService.matchVolunteerWithMissions(volunteerId, numberOfMatches))
                .thenThrow(new RuntimeException("Error inesperado"));

        ResponseEntity<List<RankedOrganizationResponse>> response = volunteerController.matchVolunteerWithOrganizations(volunteerId, numberOfMatches);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        verify(volunteerService, times(1)).matchVolunteerWithMissions(volunteerId, numberOfMatches);
        verify(rankedOrganizationMapper, never()).toResponses(anyList());
    }

    @Test
    void testMatchVolunteerWithOrganizations_Success() {
        Integer volunteerId = 1;
        int numberOfMatches = 5;

        List<RankedOrganizationResponse> rankedOrganizations = List.of(new RankedOrganizationResponse());

        when(volunteerService.matchVolunteerWithMissions(volunteerId, numberOfMatches)).thenReturn(new ArrayList<>());
        when(rankedOrganizationMapper.toResponses(anyList())).thenReturn(rankedOrganizations);

        ResponseEntity<List<RankedOrganizationResponse>> response = volunteerController.matchVolunteerWithOrganizations(volunteerId, numberOfMatches);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(volunteerService, times(1)).matchVolunteerWithMissions(volunteerId, numberOfMatches);
        verify(rankedOrganizationMapper, times(1)).toResponses(anyList());
    }

    @Test
    void testGetLastFiveVolunteers() {
        VolunteerEntity volunteer1 = new VolunteerEntity();
        volunteer1.setId(1);
        VolunteerEntity volunteer2 = new VolunteerEntity();
        volunteer2.setId(2);

        List<VolunteerEntity> volunteers = List.of(volunteer1, volunteer2);

        when(volunteerService.getLastFiveVolunteers()).thenReturn(volunteers);

        ResponseEntity<List<VolunteerEntity>> response = volunteerController.getLastFiveVolunteers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(volunteer1, response.getBody().get(0));
        assertEquals(volunteer2, response.getBody().get(1));

        verify(volunteerService, times(1)).getLastFiveVolunteers();
    }
}
