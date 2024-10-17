package com.constructiveactivists.volunteermodule.controllers;

import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PostulationControllerTest {

    @Mock
    private PostulationService postulationService;

    @InjectMocks
    private PostulationController postulationController;

    private PostulationEntity postulationEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        postulationEntity = new PostulationEntity();
        postulationEntity.setVolunteerOrganizationId(1);
        postulationEntity.setStatus(OrganizationStatusEnum.PENDIENTE);
        postulationEntity.setRegistrationDate(LocalDate.now());
    }

    @Test
    void testGetPendingPostulationsByOrganizationId_Success() {
        List<PostulationEntity> postulations = new ArrayList<>();
        postulations.add(postulationEntity);

        when(postulationService.getPendingPostulationsByOrganizationId(1)).thenReturn(postulations);

        ResponseEntity<List<PostulationEntity>> response = postulationController.getPendingPostulationsByOrganizationId(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals(OrganizationStatusEnum.PENDIENTE, response.getBody().get(0).getStatus());
        verify(postulationService, times(1)).getPendingPostulationsByOrganizationId(1);
    }

    @Test
    void testGetPendingPostulationsByOrganizationId_NoContent() {

        when(postulationService.getPendingPostulationsByOrganizationId(1)).thenReturn(new ArrayList<>());

        ResponseEntity<List<PostulationEntity>> response = postulationController.getPendingPostulationsByOrganizationId(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, Objects.requireNonNull(response.getBody()).size());
        verify(postulationService, times(1)).getPendingPostulationsByOrganizationId(1);
    }
}
