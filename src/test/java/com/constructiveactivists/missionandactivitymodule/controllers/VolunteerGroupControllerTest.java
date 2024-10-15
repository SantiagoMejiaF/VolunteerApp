package com.constructiveactivists.missionandactivitymodule.controllers;

import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupEntity;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class VolunteerGroupControllerTest {

    @Mock
    private VolunteerGroupService volunteerGroupService;

    @InjectMocks
    private VolunteerGroupController volunteerGroupController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllVolunteerGroups() {

        List<VolunteerGroupEntity> volunteerGroups = new ArrayList<>();
        VolunteerGroupEntity group1 = new VolunteerGroupEntity();
        group1.setId(1);
        group1.setName("Grupo 1");
        volunteerGroups.add(group1);

        VolunteerGroupEntity group2 = new VolunteerGroupEntity();
        group2.setId(2);
        group2.setName("Grupo 2");
        volunteerGroups.add(group2);

        when(volunteerGroupService.getAllVolunteerGroups()).thenReturn(volunteerGroups);

        ResponseEntity<List<VolunteerGroupEntity>> response = volunteerGroupController.getAllVolunteerGroups();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Grupo 1", response.getBody().get(0).getName());
        assertEquals("Grupo 2", response.getBody().get(1).getName());

        verify(volunteerGroupService, times(1)).getAllVolunteerGroups();
    }

    @Test
    void testGetVolunteerGroupById_Success() {

        VolunteerGroupEntity volunteerGroup = new VolunteerGroupEntity();
        volunteerGroup.setId(1);
        volunteerGroup.setName("Grupo de Prueba");

        when(volunteerGroupService.getVolunteerGroupById(1)).thenReturn(Optional.of(volunteerGroup));

        ResponseEntity<VolunteerGroupEntity> response = volunteerGroupController.getVolunteerGroupById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Grupo de Prueba", response.getBody().getName());

        verify(volunteerGroupService, times(1)).getVolunteerGroupById(1);
    }

    @Test
    void testGetVolunteerGroupById_NotFound() {

        when(volunteerGroupService.getVolunteerGroupById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<VolunteerGroupEntity> response = volunteerGroupController.getVolunteerGroupById(99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(volunteerGroupService, times(1)).getVolunteerGroupById(99);
    }

    @Test
    void testGetVolunteerGroupsByOrganizationId() {

        List<VolunteerGroupEntity> volunteerGroups = new ArrayList<>();
        VolunteerGroupEntity group1 = new VolunteerGroupEntity();
        group1.setId(1);
        group1.setOrganizationId(10);
        group1.setName("Grupo 1");

        VolunteerGroupEntity group2 = new VolunteerGroupEntity();
        group2.setId(2);
        group2.setOrganizationId(10);
        group2.setName("Grupo 2");

        volunteerGroups.add(group1);
        volunteerGroups.add(group2);

        when(volunteerGroupService.getVolunteerGroupByOrganizationId(10)).thenReturn(volunteerGroups);

        ResponseEntity<List<VolunteerGroupEntity>> response = volunteerGroupController.getVolunteerGroupsByOrganizationId(10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Grupo 1", response.getBody().get(0).getName());
        assertEquals("Grupo 2", response.getBody().get(1).getName());

        verify(volunteerGroupService, times(1)).getVolunteerGroupByOrganizationId(10);
    }
}
