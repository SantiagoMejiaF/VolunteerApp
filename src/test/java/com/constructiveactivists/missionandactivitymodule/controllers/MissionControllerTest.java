package com.constructiveactivists.missionandactivitymodule.controllers;

import com.constructiveactivists.missionandactivitymodule.controllers.request.mission.MissionRequest;
import com.constructiveactivists.missionandactivitymodule.controllers.request.mission.MissionUpdateRequest;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionTypeEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VolunteerMissionRequirementsEnum;
import com.constructiveactivists.missionandactivitymodule.mappers.mission.MissionMapper;
import com.constructiveactivists.missionandactivitymodule.services.mission.MissionService;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MissionControllerTest {

    @Mock
    private MissionService missionService;

    @Mock
    private MissionMapper missionMapper;

    @InjectMocks
    private MissionController missionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMissions() {
        MissionEntity mission1 = new MissionEntity();
        MissionEntity mission2 = new MissionEntity();

        when(missionService.getAllMisions()).thenReturn(List.of(mission1, mission2));

        ResponseEntity<List<MissionEntity>> response = missionController.getAllMissions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        verify(missionService, times(1)).getAllMisions();
    }

    @Test
    void testGetMissionById_Success() {
        MissionEntity mission = new MissionEntity();
        when(missionService.getMissionById(1)).thenReturn(Optional.of(mission));

        ResponseEntity<MissionEntity> response = missionController.getMissionById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(missionService, times(1)).getMissionById(1);
    }

    @Test
    void testGetMissionById_NotFound() {
        when(missionService.getMissionById(1)).thenReturn(Optional.empty());

        ResponseEntity<MissionEntity> response = missionController.getMissionById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(missionService, times(1)).getMissionById(1);
    }

    @Test
    void testCreateMission() {
        MissionRequest missionRequest = new MissionRequest();
        MissionEntity mission = new MissionEntity();

        when(missionMapper.toDomain(missionRequest)).thenReturn(mission);
        when(missionService.save(mission)).thenReturn(mission);

        ResponseEntity<MissionEntity> response = missionController.createMission(missionRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(missionMapper, times(1)).toDomain(missionRequest);
        verify(missionService, times(1)).save(mission);
    }

    @Test
    void testGetMissionsByOrganizationId() {
        MissionEntity mission1 = new MissionEntity();
        MissionEntity mission2 = new MissionEntity();

        when(missionService.getMissionsByOrganizationId(1)).thenReturn(List.of(mission1, mission2));

        ResponseEntity<List<MissionEntity>> response = missionController.getMissionsByOrganizationId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(missionService, times(1)).getMissionsByOrganizationId(1);
    }

    @Test
    void testGetMissionTypes() {
        List<MissionTypeEnum> missionTypes = List.of(MissionTypeEnum.SALUD, MissionTypeEnum.EDUCACION);

        when(missionService.getMissionTypes()).thenReturn(missionTypes);

        ResponseEntity<List<MissionTypeEnum>> response = missionController.getMissionTypes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        verify(missionService, times(1)).getMissionTypes();
    }

    @Test
    void testGetVisibilityOptions() {
        List<VisibilityEnum> visibilityOptions = List.of(VisibilityEnum.PUBLICA, VisibilityEnum.PRIVADA);

        when(missionService.getVisibilityOptions()).thenReturn(visibilityOptions);

        ResponseEntity<List<VisibilityEnum>> response = missionController.getVisibilityOptions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        verify(missionService, times(1)).getVisibilityOptions();
    }

    @Test
    void testGetMissionStatusOptions() {
        List<MissionStatusEnum> missionStatusOptions = List.of(MissionStatusEnum.DISPONIBLE, MissionStatusEnum.CANCELADA);

        when(missionService.getMissionStatusOptions()).thenReturn(missionStatusOptions);

        ResponseEntity<List<MissionStatusEnum>> response = missionController.getMissionStatusOptions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        verify(missionService, times(1)).getMissionStatusOptions();
    }

    @Test
    void testGetVolunteerRequirements() {
        List<VolunteerMissionRequirementsEnum> requirements = List.of(VolunteerMissionRequirementsEnum.EXPERIENCIA_PREVIA);

        when(missionService.getVolunteerRequirements()).thenReturn(requirements);

        ResponseEntity<List<VolunteerMissionRequirementsEnum>> response = missionController.getVolunteerRequirements();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        verify(missionService, times(1)).getVolunteerRequirements();
    }

    @Test
    void testGetRequiredSkills() {
        List<SkillEnum> requiredSkills = List.of(SkillEnum.COMUNICACION, SkillEnum.LIDERAZGO);

        when(missionService.getRequiredSkills()).thenReturn(requiredSkills);

        ResponseEntity<List<SkillEnum>> response = missionController.getRequiredSkills();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        verify(missionService, times(1)).getRequiredSkills();
    }

    @Test
    void testCancelMission_Success() {
        doNothing().when(missionService).cancelMissionById(1);

        ResponseEntity<Void> response = missionController.cancelMission(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(missionService, times(1)).cancelMissionById(1);
    }

    @Test
    void testCancelMission_NotFound() {
        doThrow(new EntityNotFoundException()).when(missionService).cancelMissionById(1);

        ResponseEntity<Void> response = missionController.cancelMission(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(missionService, times(1)).cancelMissionById(1);
    }

    @Test
    void testGetAllActivitiesByOrganizationId() {
        ActivityEntity activity1 = new ActivityEntity();
        ActivityEntity activity2 = new ActivityEntity();

        when(missionService.getAllActivitiesByOrganizationId(1)).thenReturn(List.of(activity1, activity2));

        ResponseEntity<List<ActivityEntity>> response = missionController.getAllActivitiesByOrganizationId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        verify(missionService, times(1)).getAllActivitiesByOrganizationId(1);
    }

    @Test
    void testGetAllActivitiesByOrganizationId_InternalServerError() {

        when(missionService.getAllActivitiesByOrganizationId(1)).thenThrow(new RuntimeException("Error inesperado"));

        ResponseEntity<List<ActivityEntity>> response = missionController.getAllActivitiesByOrganizationId(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        verify(missionService, times(1)).getAllActivitiesByOrganizationId(1);
    }

    @Test
    void testCancelMission_InternalServerError() {

        doThrow(new RuntimeException("Error inesperado")).when(missionService).cancelMissionById(1);

        ResponseEntity<Void> response = missionController.cancelMission(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        verify(missionService, times(1)).cancelMissionById(1);
    }

    @Test
    void testGetLastThreeMissions_Success() {
        MissionEntity mission1 = new MissionEntity();
        mission1.setId(1);
        mission1.setTitle("Misión 1");

        MissionEntity mission2 = new MissionEntity();
        mission2.setId(2);
        mission2.setTitle("Misión 2");

        MissionEntity mission3 = new MissionEntity();
        mission3.setId(3);
        mission3.setTitle("Misión 3");

        when(missionService.getLastThreeMissions()).thenReturn(List.of(mission1, mission2, mission3));

        ResponseEntity<List<MissionEntity>> response = missionController.getLastThreeMissions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertEquals(1, response.getBody().get(0).getId());
        assertEquals(2, response.getBody().get(1).getId());
        assertEquals(3, response.getBody().get(2).getId());

        verify(missionService, times(1)).getLastThreeMissions();
    }

    @Test
    void testUpdateMission_Success() {
        Integer missionId = 1;
        MissionUpdateRequest missionUpdateRequest = new MissionUpdateRequest();
        MissionEntity updatedMission = new MissionEntity();
        updatedMission.setId(missionId);
        updatedMission.setTitle("Updated Title");

        when(missionMapper.toEntity(missionUpdateRequest)).thenReturn(updatedMission);
        when(missionService.updateMission(missionId, updatedMission)).thenReturn(updatedMission);

        ResponseEntity<MissionEntity> response = missionController.updateMission(missionId, missionUpdateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Title", response.getBody().getTitle());

        verify(missionMapper, times(1)).toEntity(missionUpdateRequest);
        verify(missionService, times(1)).updateMission(missionId, updatedMission);
    }

    @Test
    void testUpdateMission_NotFound() {
        Integer missionId = 1;
        MissionUpdateRequest missionUpdateRequest = new MissionUpdateRequest();
        MissionEntity updatedMission = new MissionEntity();

        when(missionMapper.toEntity(missionUpdateRequest)).thenReturn(updatedMission);
        doThrow(new EntityNotFoundException()).when(missionService).updateMission(missionId, updatedMission);

        ResponseEntity<MissionEntity> response = missionController.updateMission(missionId, missionUpdateRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(missionMapper, times(1)).toEntity(missionUpdateRequest);
        verify(missionService, times(1)).updateMission(missionId, updatedMission);
    }
}
