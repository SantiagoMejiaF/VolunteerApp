package com.constructiveactivists.missionandactivitymodule.services.mission;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymodule.repositories.MissionRepository;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MissionServiceTest {

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private ActivityService activityService;

    @InjectMocks
    private MissionService missionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveMission_Success() {
        MissionEntity mission = new MissionEntity();
        mission.setOrganizationId(1);

        OrganizationEntity organization = new OrganizationEntity();
        when(organizationService.getOrganizationById(mission.getOrganizationId())).thenReturn(Optional.of(organization));

        when(missionRepository.save(mission)).thenReturn(mission);

        MissionEntity savedMission = missionService.save(mission);

        assertNotNull(savedMission);
        assertEquals(MissionStatusEnum.DISPONIBLE, savedMission.getMissionStatus());
        verify(missionRepository, times(1)).save(mission);
    }


    @Test
    void testSaveMission_OrganizationNotFound() {
        MissionEntity mission = new MissionEntity();
        mission.setOrganizationId(999);

        when(organizationService.getOrganizationById(mission.getOrganizationId())).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            missionService.save(mission);
        });

        assertEquals("La organización con el ID 999 no existe en la base de datos.", thrown.getMessage());
        verify(missionRepository, never()).save(mission);
    }

    @Test
    void testGetMissionById_Success() {
        MissionEntity mission = new MissionEntity();
        mission.setId(1);

        when(missionRepository.findById(1)).thenReturn(Optional.of(mission));

        Optional<MissionEntity> result = missionService.getMissionById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(missionRepository, times(1)).findById(1);
    }

    @Test
    void testGetMissionById_NotFound() {
        when(missionRepository.findById(999)).thenReturn(Optional.empty());

        Optional<MissionEntity> result = missionService.getMissionById(999);

        assertFalse(result.isPresent());
        verify(missionRepository, times(1)).findById(999);
    }

    @Test
    void testCancelMissionById_Success() {
        MissionEntity mission = new MissionEntity();
        mission.setId(1);

        ActivityEntity activity1 = new ActivityEntity();
        activity1.setId(1);
        ActivityEntity activity2 = new ActivityEntity();
        activity2.setId(2);

        List<ActivityEntity> activities = Arrays.asList(activity1, activity2);

        when(missionRepository.findById(1)).thenReturn(Optional.of(mission));
        when(activityService.getActivitiesByMissionId(1)).thenReturn(activities);

        missionService.cancelMissionById(1);

        assertEquals(MissionStatusEnum.CANCELADA, mission.getMissionStatus());
        verify(activityService, times(1)).deleteActivityById(1);
        verify(activityService, times(1)).deleteActivityById(2);
        verify(missionRepository, times(1)).save(mission);
    }

    @Test
    void testCancelMissionById_NotFound() {
        when(missionRepository.findById(999)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            missionService.cancelMissionById(999);
        });

        assertEquals("Mission with ID 999 not found", thrown.getMessage());
        verify(missionRepository, times(1)).findById(999);
        verify(missionRepository, never()).save(any(MissionEntity.class));
    }

    @Test
    void testGetAllMissions_Success() {
        MissionEntity mission1 = new MissionEntity();
        MissionEntity mission2 = new MissionEntity();

        when(missionRepository.findAll()).thenReturn(Arrays.asList(mission1, mission2));

        List<MissionEntity> result = missionService.getAllMisions();

        assertEquals(2, result.size());
        verify(missionRepository, times(1)).findAll();
    }

    @Test
    void testGetMissionsByOrganizationId_Success() {
        MissionEntity mission1 = new MissionEntity();
        MissionEntity mission2 = new MissionEntity();

        when(missionRepository.findByOrganizationId(1)).thenReturn(Arrays.asList(mission1, mission2));

        List<MissionEntity> result = missionService.getMissionsByOrganizationId(1);

        assertEquals(2, result.size());
        verify(missionRepository, times(1)).findByOrganizationId(1);
    }
}
