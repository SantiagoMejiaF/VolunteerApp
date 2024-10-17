package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.PersonalDataCommunityLeaderEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.enums.ActivityStatusEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupEntity;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupMembershipEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.*;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupService;
import com.constructiveactivists.organizationmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import com.constructiveactivists.organizationmodule.repositories.ActivityCoordinatorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ActivityServiceTest {

    @Mock
    private VolunteerGroupService volunteerGroupService;

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private VolunteerGroupRepository groupRepository;

    @Mock
    private VolunteerGroupMembershipRepository membershipRepository;

    @Mock
    private QRCodeService qrCodeService;

    @Mock
    private ActivityCoordinatorRepository activityCoordinatorRepository;

    @Mock
    private ReviewEmailService reviewEmailService;

    @InjectMocks
    private ActivityService activityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveActivity_Success() {

        MissionEntity mission = new MissionEntity();
        mission.setId(1);
        mission.setOrganizationId(100);

        ActivityCoordinatorEntity coordinator = new ActivityCoordinatorEntity();
        coordinator.setId(1);

        ActivityEntity activity = getActivityEntity(mission, coordinator);

        VolunteerGroupEntity volunteerGroup = new VolunteerGroupEntity();
        volunteerGroup.setId(1);

        when(missionRepository.getMissionById(activity.getMissionId())).thenReturn(Optional.of(mission));
        when(activityCoordinatorRepository.findById(activity.getActivityCoordinator())).thenReturn(Optional.of(coordinator));
        when(volunteerGroupService.save(any(VolunteerGroupEntity.class))).thenReturn(volunteerGroup);
        when(activityRepository.save(activity)).thenReturn(activity);

        ActivityEntity savedActivity = activityService.save(activity);

        assertNotNull(savedActivity);
        assertEquals(ActivityStatusEnum.DISPONIBLE, savedActivity.getActivityStatus());
        verify(activityRepository, times(2)).save(activity);
        verify(volunteerGroupService, times(2)).save(any(VolunteerGroupEntity.class));
        verify(reviewEmailService, times(1)).sendFormEmail(eq("leader@example.com"), anyInt());
    }

    private ActivityEntity getActivityEntity(MissionEntity mission, ActivityCoordinatorEntity coordinator) {
        PersonalDataCommunityLeaderEntity leader = new PersonalDataCommunityLeaderEntity();
        leader.setEmailCommunityLeader("leader@example.com");

        ActivityEntity activity = new ActivityEntity();
        activity.setId(1);
        activity.setMissionId(mission.getId());
        activity.setActivityCoordinator(coordinator.getId());
        activity.setNumberOfVolunteersRequired(5);
        activity.setTitle("Plantación de árboles");
        activity.setPersonalDataCommunityLeaderEntity(leader);
        return activity;
    }

    @Test
    void testSaveActivity_MissionNotFound() {
        ActivityEntity activity = new ActivityEntity();
        activity.setMissionId(999);

        when(missionRepository.getMissionById(activity.getMissionId())).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            activityService.save(activity);
        });

        assertEquals("La misión con el ID: 999 no existe en la base de datos.", thrown.getMessage());
        verify(missionRepository, times(1)).getMissionById(999);
        verify(activityRepository, never()).save(any(ActivityEntity.class));
    }

    @Test
    void testSaveActivity_CoordinatorNotFound() {
        MissionEntity mission = new MissionEntity();
        mission.setId(1);

        ActivityEntity activity = new ActivityEntity();
        activity.setMissionId(1);
        activity.setActivityCoordinator(999);

        when(missionRepository.getMissionById(activity.getMissionId())).thenReturn(Optional.of(mission));
        when(activityCoordinatorRepository.findById(activity.getActivityCoordinator())).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            activityService.save(activity);
        });

        assertEquals("El coordinador con el ID 999 no existe en la base de datos.", thrown.getMessage());
        verify(activityCoordinatorRepository, times(1)).findById(999);
        verify(activityRepository, never()).save(any(ActivityEntity.class));
    }

    @Test
    void testGetActivityById_Success() {
        ActivityEntity activity = new ActivityEntity();
        activity.setId(1);

        when(activityRepository.findById(1)).thenReturn(Optional.of(activity));

        Optional<ActivityEntity> result = activityService.getById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(activityRepository, times(1)).findById(1);
    }

    @Test
    void testGetActivityById_NotFound() {
        when(activityRepository.findById(999)).thenReturn(Optional.empty());

        Optional<ActivityEntity> result = activityService.getById(999);

        assertFalse(result.isPresent());
        verify(activityRepository, times(1)).findById(999);
    }

    @Test
    void testDeleteActivityById_Success() {
        ActivityEntity activity = new ActivityEntity();
        activity.setId(1);

        when(activityRepository.findById(1)).thenReturn(Optional.of(activity));

        activityService.deleteActivityById(1);

        verify(volunteerGroupService, times(1)).deleteVolunteerGroupById(1);
        verify(activityRepository, times(2)).findById(1);
        verify(activityRepository, times(1)).save(activity);
    }

    @Test
    void testUpdateActivityStatus_Success() {
        ActivityEntity activity = new ActivityEntity();
        activity.setId(1);
        activity.setActivityStatus(ActivityStatusEnum.DISPONIBLE);

        when(activityRepository.findById(1)).thenReturn(Optional.of(activity));

        activityService.updateActivityStatus(1, ActivityStatusEnum.CANCELADA);

        assertEquals(ActivityStatusEnum.CANCELADA, activity.getActivityStatus());
        verify(activityRepository, times(1)).save(activity);
    }

    @Test
    void testUpdateActivityStatus_NotFound() {
        when(activityRepository.findById(999)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            activityService.updateActivityStatus(999, ActivityStatusEnum.CANCELADA);
        });

        assertEquals("Activity with id 999 not found", thrown.getMessage());
        verify(activityRepository, times(1)).findById(999);
    }

    @Test
    void testGetAllActivities() {
        ActivityEntity activity1 = new ActivityEntity();
        activity1.setId(1);
        ActivityEntity activity2 = new ActivityEntity();
        activity2.setId(2);

        when(activityRepository.findAll()).thenReturn(List.of(activity1, activity2));

        List<ActivityEntity> activities = activityService.getAll();

        assertNotNull(activities);
        assertEquals(2, activities.size());
        verify(activityRepository, times(1)).findAll();
    }

    @Test
    void testFindAllByActivityCoordinator() {
        ActivityEntity activity1 = new ActivityEntity();
        activity1.setId(1);

        when(activityRepository.findAllByActivityCoordinator(1)).thenReturn(List.of(activity1));

        List<ActivityEntity> activities = activityService.findAllByActivityCoordinator(1);

        assertNotNull(activities);
        assertEquals(1, activities.size());
        verify(activityRepository, times(1)).findAllByActivityCoordinator(1);
    }

    @Test
    void testGetCheckInQrCode() {
        byte[] qrCode = new byte[]{1, 2, 3};
        when(qrCodeService.generateCheckInQrCode(1)).thenReturn(qrCode);

        byte[] result = activityService.getCheckInQrCode(1);

        assertNotNull(result);
        assertArrayEquals(qrCode, result);
        verify(qrCodeService, times(1)).generateCheckInQrCode(1);
    }

    @Test
    void testGetCheckOutQrCode() {
        byte[] qrCode = new byte[]{4, 5, 6};
        when(qrCodeService.generateCheckOutQrCode(1)).thenReturn(qrCode);

        byte[] result = activityService.getCheckOutQrCode(1);

        assertNotNull(result);
        assertArrayEquals(qrCode, result);
        verify(qrCodeService, times(1)).generateCheckOutQrCode(1);
    }

    @Test
    void testGetActivitiesByMissionId() {
        ActivityEntity activity1 = new ActivityEntity();
        activity1.setId(1);

        when(activityRepository.findByMissionId(1)).thenReturn(List.of(activity1));

        List<ActivityEntity> activities = activityService.getActivitiesByMissionId(1);

        assertNotNull(activities);
        assertEquals(1, activities.size());
        verify(activityRepository, times(1)).findByMissionId(1);
    }

    @Test
    void testGetActivitiesByVolunteerId() {
        VolunteerGroupMembershipEntity membership = new VolunteerGroupMembershipEntity();
        membership.setGroupId(1);

        VolunteerGroupEntity group = new VolunteerGroupEntity();
        group.setActivity(1);

        ActivityEntity activity = new ActivityEntity();
        activity.setId(1);

        when(membershipRepository.findByVolunteerId(1)).thenReturn(List.of(membership));
        when(groupRepository.findByIdIn(List.of(1))).thenReturn(List.of(group));
        when(activityRepository.findByIdIn(List.of(1))).thenReturn(List.of(activity));

        List<ActivityEntity> activities = activityService.getActivitiesByVolunteerId(1);

        assertNotNull(activities);
        assertEquals(1, activities.size());
        verify(membershipRepository, times(1)).findByVolunteerId(1);
        verify(groupRepository, times(1)).findByIdIn(List.of(1));
        verify(activityRepository, times(1)).findByIdIn(List.of(1));
    }
}
