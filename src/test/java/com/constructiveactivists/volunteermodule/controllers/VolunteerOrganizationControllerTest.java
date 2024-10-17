package com.constructiveactivists.volunteermodule.controllers;

import com.constructiveactivists.volunteermodule.controllers.request.volunteerorganization.VolunteerOrganizationRequest;
import com.constructiveactivists.volunteermodule.controllers.response.StatusVolunteerOrganizationResponse;
import com.constructiveactivists.volunteermodule.controllers.response.VolunteerOrganizationResponse;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.mappers.volunteerorganization.VolunteerOrganizationMapper;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.VolunteerOrganizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VolunteerOrganizationControllerTest {

    @Mock
    private VolunteerOrganizationService volunteerOrganizationService;

    @Mock
    private VolunteerOrganizationMapper volunteerOrganizationMapper;

    @InjectMocks
    private VolunteerOrganizationController volunteerOrganizationController;

    private VolunteerOrganizationEntity volunteerOrganizationEntity;
    private VolunteerOrganizationRequest volunteerOrganizationRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        volunteerOrganizationEntity = new VolunteerOrganizationEntity();
        volunteerOrganizationEntity.setId(1);
        volunteerOrganizationRequest = new VolunteerOrganizationRequest();
    }

    @Test
    void testGetOrganizationsByVolunteerId() {
        List<VolunteerOrganizationEntity> organizationEntities = List.of(volunteerOrganizationEntity);
        when(volunteerOrganizationService.getOrganizationsByVolunteerId(1)).thenReturn(organizationEntities);

        ResponseEntity<List<VolunteerOrganizationEntity>> response = volunteerOrganizationController.getOrganizationsByVolunteerId(1);

        assertEquals(organizationEntities, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(volunteerOrganizationService, times(1)).getOrganizationsByVolunteerId(1);
    }

    @Test
    void testGetVolunteersByOrganizationId() {
        List<VolunteerOrganizationEntity> volunteerEntities = List.of(volunteerOrganizationEntity);
        when(volunteerOrganizationService.getVolunteersByOrganizationId(1)).thenReturn(volunteerEntities);

        ResponseEntity<List<VolunteerOrganizationEntity>> response = volunteerOrganizationController.getVolunteersByOrganizationId(1);

        assertEquals(volunteerEntities, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(volunteerOrganizationService, times(1)).getVolunteersByOrganizationId(1);
    }

    @Test
    void testAddVolunteerOrganizationPending() {
        when(volunteerOrganizationMapper.toEntity(volunteerOrganizationRequest)).thenReturn(volunteerOrganizationEntity);
        when(volunteerOrganizationService.addVolunteerOrganizationPending(any(VolunteerOrganizationEntity.class)))
                .thenReturn(volunteerOrganizationEntity);

        ResponseEntity<VolunteerOrganizationEntity> response = volunteerOrganizationController
                .addVolunteerOrganizationPending(volunteerOrganizationRequest);

        assertEquals(volunteerOrganizationEntity, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(volunteerOrganizationService, times(1))
                .addVolunteerOrganizationPending(any(VolunteerOrganizationEntity.class));
    }

    @Test
    void testGetVolunteerOrganizationDetails() {
        VolunteerOrganizationResponse volunteerOrganizationResponse = new VolunteerOrganizationResponse();
        when(volunteerOrganizationService.getVolunteerOrganizationDetails(1)).thenReturn(volunteerOrganizationResponse);

        ResponseEntity<VolunteerOrganizationResponse> response = volunteerOrganizationController
                .getVolunteerOrganizationDetails(1);

        assertEquals(volunteerOrganizationResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(volunteerOrganizationService, times(1)).getVolunteerOrganizationDetails(1);
    }

    @Test
    void testGetPendingVolunteersByOrganizationId() {
        List<StatusVolunteerOrganizationResponse> pendingVolunteers = List.of(new StatusVolunteerOrganizationResponse());
        when(volunteerOrganizationService.getPendingVolunteersByOrganizationId(1)).thenReturn(pendingVolunteers);

        ResponseEntity<List<StatusVolunteerOrganizationResponse>> response = volunteerOrganizationController
                .getPendingVolunteersByOrganizationId(1);

        assertEquals(pendingVolunteers, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(volunteerOrganizationService, times(1)).getPendingVolunteersByOrganizationId(1);
    }

    @Test
    void testGetAcceptedVolunteersByOrganizationId() {
        List<StatusVolunteerOrganizationResponse> acceptedVolunteers = List.of(new StatusVolunteerOrganizationResponse());
        when(volunteerOrganizationService.getAcceptedVolunteersByOrganizationId(1)).thenReturn(acceptedVolunteers);

        ResponseEntity<List<StatusVolunteerOrganizationResponse>> response = volunteerOrganizationController
                .getAcceptedVolunteersByOrganizationId(1);

        assertEquals(acceptedVolunteers, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(volunteerOrganizationService, times(1)).getAcceptedVolunteersByOrganizationId(1);
    }

    @Test
    void testGetRejectedVolunteersByOrganizationId() {
        List<StatusVolunteerOrganizationResponse> rejectedVolunteers = List.of(new StatusVolunteerOrganizationResponse());
        when(volunteerOrganizationService.getRejectedVolunteersByOrganizationId(1)).thenReturn(rejectedVolunteers);

        ResponseEntity<List<StatusVolunteerOrganizationResponse>> response = volunteerOrganizationController
                .getRejectedVolunteersByOrganizationId(1);

        assertEquals(rejectedVolunteers, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(volunteerOrganizationService, times(1)).getRejectedVolunteersByOrganizationId(1);
    }
}
