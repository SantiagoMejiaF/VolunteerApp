package com.constructiveactivists.volunteermodule.controllers;

import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.DataShareVolunteerOrganizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DataShareVolunteerOrganizationControllerTest {

    @Mock
    private DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;

    @InjectMocks
    private DataShareVolunteerOrganizationController dataShareVolunteerOrganizationController;

    private DataShareVolunteerOrganizationEntity entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        entity = new DataShareVolunteerOrganizationEntity();
        entity.setVolunteerOrganizationId(1);
        entity.setHoursDone(40);
        entity.setHoursCertified(30);
        entity.setMonthlyHours(10);
    }

    @Test
    void testFindAll() {

        List<DataShareVolunteerOrganizationEntity> entities = new ArrayList<>();
        entities.add(entity);

        when(dataShareVolunteerOrganizationService.findAll()).thenReturn(entities);

        ResponseEntity<List<DataShareVolunteerOrganizationEntity>> response = dataShareVolunteerOrganizationController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals(40, response.getBody().get(0).getHoursDone());
        verify(dataShareVolunteerOrganizationService, times(1)).findAll();
    }

    @Test
    void testFindAll_NoContent() {

        when(dataShareVolunteerOrganizationService.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<List<DataShareVolunteerOrganizationEntity>> response = dataShareVolunteerOrganizationController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, Objects.requireNonNull(response.getBody()).size());
        verify(dataShareVolunteerOrganizationService, times(1)).findAll();
    }
}
