package com.constructiveactivists.missionandactivitymodule.services.mission;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionTypeEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VolunteerMissionRequirementsEnum;
import com.constructiveactivists.missionandactivitymodule.repositories.MissionRepository;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
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

    @Test
    void testGetMissionTypes() {
        List<MissionTypeEnum> missionTypes = missionService.getMissionTypes();

        assertEquals(Arrays.asList(MissionTypeEnum.values()), missionTypes);
    }

    @Test
    void testGetVisibilityOptions() {
        List<VisibilityEnum> visibilityOptions = missionService.getVisibilityOptions();

        assertEquals(Arrays.asList(VisibilityEnum.values()), visibilityOptions);
    }

    @Test
    void testGetMissionStatusOptions() {
        List<MissionStatusEnum> missionStatusOptions = missionService.getMissionStatusOptions();

        assertEquals(Arrays.asList(MissionStatusEnum.values()), missionStatusOptions);
    }

    @Test
    void testGetVolunteerRequirements() {
        List<VolunteerMissionRequirementsEnum> volunteerRequirements = missionService.getVolunteerRequirements();

        assertEquals(Arrays.asList(VolunteerMissionRequirementsEnum.values()), volunteerRequirements);
    }

    @Test
    void testGetRequiredSkills() {
        List<SkillEnum> requiredSkills = missionService.getRequiredSkills();

        assertEquals(Arrays.asList(SkillEnum.values()), requiredSkills);
    }

    @Test
    void testGetActivitiesByMissionId_Success() {
        MissionEntity mission = new MissionEntity();
        mission.setId(1);
        ActivityEntity activity1 = new ActivityEntity();
        activity1.setId(1);
        ActivityEntity activity2 = new ActivityEntity();
        activity2.setId(2);

        when(missionRepository.findById(1)).thenReturn(Optional.of(mission));
        when(activityService.getActivitiesByMissionId(1)).thenReturn(Arrays.asList(activity1, activity2));

        List<ActivityEntity> activities = missionService.getActivitiesByMissionId(1);

        assertEquals(2, activities.size());
        verify(activityService, times(1)).getActivitiesByMissionId(1);
    }

    @Test
    void testGetActivitiesByMissionId_NotFound() {
        when(missionRepository.findById(999)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            missionService.getActivitiesByMissionId(999);
        });

        assertEquals("Mission with ID 999 not found", thrown.getMessage());
    }

    @Test
    void testGetAllActivitiesByOrganizationId() {
        MissionEntity mission1 = new MissionEntity();
        mission1.setId(1);
        MissionEntity mission2 = new MissionEntity();
        mission2.setId(2);

        ActivityEntity activity1 = new ActivityEntity();
        activity1.setId(1);
        ActivityEntity activity2 = new ActivityEntity();
        activity2.setId(2);

        when(missionRepository.findByOrganizationId(1)).thenReturn(Arrays.asList(mission1, mission2));
        when(activityService.getActivitiesByMissionId(1)).thenReturn(List.of(activity1));
        when(activityService.getActivitiesByMissionId(2)).thenReturn(List.of(activity2));

        List<ActivityEntity> activities = missionService.getAllActivitiesByOrganizationId(1);

        assertEquals(2, activities.size());
        verify(activityService, times(1)).getActivitiesByMissionId(1);
        verify(activityService, times(1)).getActivitiesByMissionId(2);
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

        when(missionRepository.findTop3ByOrderByCreatedAtDesc()).thenReturn(Arrays.asList(mission1, mission2, mission3));

        List<MissionEntity> lastThreeMissions = missionService.getLastThreeMissions();

        assertEquals(3, lastThreeMissions.size());
        assertEquals(1, lastThreeMissions.get(0).getId());
        assertEquals(2, lastThreeMissions.get(1).getId());
        assertEquals(3, lastThreeMissions.get(2).getId());

        verify(missionRepository, times(1)).findTop3ByOrderByCreatedAtDesc();
    }

    @Test
    void testUpdateMission_Success() {
        MissionEntity existingMission = new MissionEntity();
        existingMission.setId(1);
        existingMission.setTitle("Old Title");
        existingMission.setDescription("Old Description");

        MissionEntity updatedMissionData = getMissionEntity();

        when(missionRepository.findById(1)).thenReturn(Optional.of(existingMission));
        when(missionRepository.save(existingMission)).thenReturn(existingMission);

        MissionEntity result = missionService.updateMission(1, updatedMissionData);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals(MissionTypeEnum.MEDIO_AMBIENTE, result.getMissionType());
        assertEquals(LocalDate.of(2024, 9, 1), result.getStartDate());
        assertEquals(LocalDate.of(2024, 9, 10), result.getEndDate());
        assertEquals("Cundinamarca", result.getDepartment());
        assertEquals(VisibilityEnum.PUBLICA, result.getVisibility());
        assertEquals(List.of(VolunteerMissionRequirementsEnum.EXPERIENCIA_PREVIA), result.getVolunteerMissionRequirementsEnumList());
        assertEquals(List.of(SkillEnum.TRABAJO_EN_EQUIPO), result.getRequiredSkillsList());

        verify(missionRepository, times(1)).findById(1);
        verify(missionRepository, times(1)).save(existingMission);
    }

    private MissionEntity getMissionEntity() {
        MissionEntity updatedMissionData = new MissionEntity();
        updatedMissionData.setTitle("New Title");
        updatedMissionData.setDescription("New Description");
        updatedMissionData.setMissionType(MissionTypeEnum.MEDIO_AMBIENTE);
        updatedMissionData.setStartDate(LocalDate.of(2024, 9, 1));
        updatedMissionData.setEndDate(LocalDate.of(2024, 9, 10));
        updatedMissionData.setDepartment("Cundinamarca");
        updatedMissionData.setVisibility(VisibilityEnum.PUBLICA);
        updatedMissionData.setVolunteerMissionRequirementsEnumList(List.of(VolunteerMissionRequirementsEnum.EXPERIENCIA_PREVIA));
        updatedMissionData.setRequiredSkillsList(List.of(SkillEnum.TRABAJO_EN_EQUIPO));
        return updatedMissionData;
    }

    @Test
    void testUpdateMission_NotFound() {
        MissionEntity updatedMissionData = new MissionEntity();
        updatedMissionData.setTitle("New Title");

        when(missionRepository.findById(999)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            missionService.updateMission(999, updatedMissionData);
        });

        assertEquals("Mission with ID 999 not found", thrown.getMessage());
        verify(missionRepository, times(1)).findById(999);
        verify(missionRepository, never()).save(any(MissionEntity.class));
    }
}
