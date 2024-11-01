package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.PersonalDataCommunityLeaderEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ReviewEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.enums.ActivityStatusEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupEntity;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupMembershipEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.*;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupService;
import com.constructiveactivists.organizationmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import com.constructiveactivists.organizationmodule.repositories.ActivityCoordinatorRepository;
import com.constructiveactivists.organizationmodule.services.activitycoordinator.ActivityCoordinatorService;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteeringInformationEntity;
import com.constructiveactivists.volunteermodule.repositories.VolunteerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActivityServiceTest {

    @Mock
    private VolunteerGroupService volunteerGroupService;

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private VolunteerRepository volunteerRepository;

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
    private ReviewRepository reviewRepository;

    @Mock
    private ActivityCoordinatorService activityCoordinatorService;

    @InjectMocks
    private ActivityService activityService;

    private ActivityEntity existingActivity;
    private ActivityEntity updatedActivity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        existingActivity = new ActivityEntity();
        existingActivity.setId(1);
        existingActivity.setTitle("Actividad existente");
        existingActivity.setActivityCoordinator(1);

        updatedActivity = new ActivityEntity();
        updatedActivity.setTitle("Actividad actualizada");
        updatedActivity.setActivityCoordinator(2);
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

    @Test
    void testGetAvailableActivitiesByCoordinator_Success() {
        Integer coordinatorId = 1;
        List<ActivityEntity> mockActivities = List.of(new ActivityEntity());

        when(activityRepository.findAllByActivityCoordinatorAndActivityStatus(coordinatorId, ActivityStatusEnum.DISPONIBLE))
                .thenReturn(mockActivities);

        List<ActivityEntity> result = activityService.getAvailableActivitiesByCoordinator(coordinatorId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(activityRepository, times(1)).findAllByActivityCoordinatorAndActivityStatus(coordinatorId, ActivityStatusEnum.DISPONIBLE);
    }

    @Test
    void testGetActivitiesByVolunteerAndDate() {
        ActivityEntity activity1 = new ActivityEntity();
        activity1.setId(1);
        activity1.setDate(LocalDate.of(2024, 10, 5));
        ActivityEntity activity2 = new ActivityEntity();
        activity2.setId(2);
        activity2.setDate(LocalDate.of(2024, 10, 15));
        ActivityEntity activity3 = new ActivityEntity();
        activity3.setId(3);
        activity3.setDate(LocalDate.of(2024, 9, 20));
        List<ActivityEntity> volunteerActivities = Arrays.asList(activity1, activity2, activity3);
        when(activityRepository.findByIdIn(any())).thenReturn(volunteerActivities);
        List<ActivityEntity> activitiesInOctober = activityService.getActivitiesByVolunteerAndDate(1, 10, 2024);
        assertEquals(2, activitiesInOctober.size());
        assertEquals(Arrays.asList(activity1, activity2), activitiesInOctober);
    }

    @Test
    void testGetActivitiesByVolunteerAndDate_NoActivities() {
        when(activityRepository.findByIdIn(any())).thenReturn(Collections.emptyList());
        List<ActivityEntity> activities = activityService.getActivitiesByVolunteerAndDate(1, 10, 2024);
        assertEquals(Collections.emptyList(), activities);
    }

    @Test
    void testGetTotalBeneficiariesImpactedByVolunteer_ActivitiesCompleted() {
        Integer volunteerId = 1;
        VolunteerEntity volunteer = new VolunteerEntity();
        VolunteeringInformationEntity volunteeringInfo = new VolunteeringInformationEntity();
        volunteeringInfo.setActivitiesCompleted(Arrays.asList(1, 2, 3));
        volunteer.setVolunteeringInformation(volunteeringInfo);
        ActivityEntity activity1 = new ActivityEntity();
        activity1.setNumberOfBeneficiaries(5);
        ActivityEntity activity2 = new ActivityEntity();
        activity2.setNumberOfBeneficiaries(10);
        ActivityEntity activity3 = new ActivityEntity();
        activity3.setNumberOfBeneficiaries(15);
        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));
        when(activityRepository.findById(1)).thenReturn(Optional.of(activity1));
        when(activityRepository.findById(2)).thenReturn(Optional.of(activity2));
        when(activityRepository.findById(3)).thenReturn(Optional.of(activity3));
        int totalBeneficiaries = activityService.getTotalBeneficiariesImpactedByVolunteer(volunteerId);
        assertEquals(30, totalBeneficiaries);
    }

    @Test
    void testGetTotalBeneficiariesImpactedByVolunteer_NoActivitiesCompleted() {
        Integer volunteerId = 1;
        VolunteerEntity volunteer = new VolunteerEntity();
        VolunteeringInformationEntity volunteeringInfo = new VolunteeringInformationEntity();
        volunteeringInfo.setActivitiesCompleted(Collections.emptyList());
        volunteer.setVolunteeringInformation(volunteeringInfo);
        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));
        int totalBeneficiaries = activityService.getTotalBeneficiariesImpactedByVolunteer(volunteerId);
        assertEquals(0, totalBeneficiaries);
    }

    @Test
    void testGetTotalBeneficiariesImpactedByVolunteer_VolunteerNotFound() {
        Integer volunteerId = 1;
        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.empty());
        int totalBeneficiaries = activityService.getTotalBeneficiariesImpactedByVolunteer(volunteerId);

        assertEquals(0, totalBeneficiaries);
    }

    @Test
    void testGetCompletedActivitiesCountVolunteer_VolunteerExistsWithCompletedActivities() {
        Integer volunteerId = 1;
        VolunteerEntity volunteer = new VolunteerEntity();
        VolunteeringInformationEntity volunteeringInformation = new VolunteeringInformationEntity();
        volunteeringInformation.setActivitiesCompleted(Collections.singletonList(1));

        volunteer.setVolunteeringInformation(volunteeringInformation);

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));

        int result = activityService.getCompletedActivitiesCountVolunteer(volunteerId);

        assertEquals(1, result);
    }

    @Test
    void testGetCompletedActivitiesCountVolunteer_VolunteerExistsWithNoCompletedActivities() {
        Integer volunteerId = 1;
        VolunteerEntity volunteer = new VolunteerEntity();
        VolunteeringInformationEntity volunteeringInformation = new VolunteeringInformationEntity();
        volunteeringInformation.setActivitiesCompleted(Collections.emptyList());

        volunteer.setVolunteeringInformation(volunteeringInformation);

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));

        int result = activityService.getCompletedActivitiesCountVolunteer(volunteerId);

        assertEquals(0, result);
    }

    @Test
    void testGetCompletedActivitiesCountVolunteer_VolunteerDoesNotExist() {
        Integer volunteerId = 1;
        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.empty());
        int result = activityService.getCompletedActivitiesCountVolunteer(volunteerId);
        assertEquals(0, result);
    }

    @Test
    void testgetCompletedActivitiesVolunteerList_NoVolunteers() {
        when(volunteerRepository.findAll()).thenReturn(Collections.emptyList());
        List<ActivityEntity> result = activityService.getCompletedActivitiesVolunteerList(1);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testgetCompletedActivitiesVolunteerList_NoCompletedActivities() {
        VolunteerEntity volunteer1 = new VolunteerEntity();
        volunteer1.setId(1);
        VolunteerEntity volunteer2 = new VolunteerEntity();
        volunteer2.setId(2);
        List<VolunteerEntity> volunteers = Arrays.asList(volunteer1, volunteer2);
        when(volunteerRepository.findAll()).thenReturn(volunteers);
        when(activityRepository.findByIdIn(any())).thenReturn(Collections.emptyList());
        List<ActivityEntity> result = activityService.getCompletedActivitiesVolunteerList(1);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testGetAverageRatingByVolunteer_Success() {
        Integer volunteerId = 1;

        VolunteerEntity volunteer = new VolunteerEntity();
        VolunteeringInformationEntity volunteeringInfo = new VolunteeringInformationEntity();
        volunteeringInfo.setActivitiesCompleted(List.of(1, 2, 3));
        volunteer.setVolunteeringInformation(volunteeringInfo);

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));

        ActivityEntity activity1 = new ActivityEntity();
        activity1.setId(1);
        ActivityEntity activity2 = new ActivityEntity();
        activity2.setId(2);
        ActivityEntity activity3 = new ActivityEntity();
        activity3.setId(3);

        when(activityRepository.findAllById(List.of(1, 2, 3))).thenReturn(List.of(activity1, activity2, activity3));

        ReviewEntity review1 = new ReviewEntity();
        review1.setRating(4);
        ReviewEntity review2 = new ReviewEntity();
        review2.setRating(5);
        ReviewEntity review3 = new ReviewEntity();
        review3.setRating(3);

        when(reviewRepository.findByActivityIn(List.of(activity1, activity2, activity3))).thenReturn(List.of(review1, review2, review3));

        Double averageRating = activityService.getAverageRatingByVolunteer(volunteerId);
        assertEquals(4.0, averageRating);
    }

    @Test
    void testGetAverageRatingByVolunteer_NoCompletedActivities() {
        Integer volunteerId = 1;

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () ->
                activityService.getAverageRatingByVolunteer(volunteerId)
        );

        assertEquals("El voluntario no tiene actividades completadas.", exception.getMessage());
    }

    @Test
    void testGetCompletedActivitiesVolunteerList_WithActivities() {
        Integer volunteerId = 1;
        VolunteerEntity volunteer = new VolunteerEntity();
        VolunteeringInformationEntity volunteeringInfo = new VolunteeringInformationEntity();
        volunteeringInfo.setActivitiesCompleted(List.of(1, 2, 3));
        volunteer.setVolunteeringInformation(volunteeringInfo);

        List<ActivityEntity> activities = List.of(new ActivityEntity(), new ActivityEntity(), new ActivityEntity());

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));
        when(activityRepository.findAllById(volunteeringInfo.getActivitiesCompleted())).thenReturn(activities);

        List<ActivityEntity> result = activityService.getCompletedActivitiesVolunteerList(volunteerId);

        assertEquals(3, result.size());
    }

    @Test
    void testGetCompletedActivitiesVolunteerList_NoActivities() {
        Integer volunteerId = 1;
        VolunteerEntity volunteer = new VolunteerEntity();
        VolunteeringInformationEntity volunteeringInfo = new VolunteeringInformationEntity();
        volunteeringInfo.setActivitiesCompleted(Collections.emptyList());
        volunteer.setVolunteeringInformation(volunteeringInfo);

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));

        List<ActivityEntity> result = activityService.getCompletedActivitiesVolunteerList(volunteerId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetCompletedActivitiesVolunteerList_VolunteerNotFound() {
        Integer volunteerId = 1;

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.empty());

        List<ActivityEntity> result = activityService.getCompletedActivitiesVolunteerList(volunteerId);

        assertTrue(result.isEmpty());
    }


    @Test
    void testGetActivitiesCountByVolunteerAndYearInSpanish_WithActivities() {
        Integer volunteerId = 1;
        int year = 2023;

        ActivityEntity activity1 = new ActivityEntity();
        activity1.setDate(LocalDate.of(2023, 1, 15));
        ActivityEntity activity2 = new ActivityEntity();
        activity2.setDate(LocalDate.of(2023, 2, 5));
        ActivityEntity activity3 = new ActivityEntity();
        activity3.setDate(LocalDate.of(2023, 1, 20));

        when(activityService.getActivitiesByVolunteerId(volunteerId)).thenReturn(List.of(activity1, activity2, activity3));

        Map<String, Long> result = activityService.getActivitiesCountByVolunteerAndYearInSpanish(volunteerId, year);

        assertEquals(2L, result.get("ENERO"));
        assertEquals(1L, result.get("FEBRERO"));
        assertEquals(0L, result.get("MARZO"));
    }

    @Test
    void testGetActivitiesCountByVolunteerAndYearInSpanish_NoActivitiesInYear() {
        Integer volunteerId = 1;
        int year = 2023;

        ActivityEntity activity1 = new ActivityEntity();
        activity1.setDate(LocalDate.of(2022, 12, 30));
        ActivityEntity activity2 = new ActivityEntity();
        activity2.setDate(LocalDate.of(2021, 1, 1));

        when(activityService.getActivitiesByVolunteerId(volunteerId)).thenReturn(List.of(activity1, activity2));

        Map<String, Long> result = activityService.getActivitiesCountByVolunteerAndYearInSpanish(volunteerId, year);

        assertTrue(result.values().stream().allMatch(count -> count == 0L));
    }

    @Test
    void testGetAverageRatingByVolunteer_NoAvailableReviews() {
        Integer volunteerId = 1;

        VolunteerEntity volunteer = new VolunteerEntity();
        VolunteeringInformationEntity volunteeringInfo = new VolunteeringInformationEntity();
        volunteeringInfo.setActivitiesCompleted(List.of(1, 2, 3));
        volunteer.setVolunteeringInformation(volunteeringInfo);

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));

        ActivityEntity activity1 = new ActivityEntity();
        activity1.setId(1);
        ActivityEntity activity2 = new ActivityEntity();
        activity2.setId(2);
        ActivityEntity activity3 = new ActivityEntity();
        activity3.setId(3);

        when(activityRepository.findAllById(List.of(1, 2, 3))).thenReturn(List.of(activity1, activity2, activity3));
        when(reviewRepository.findByActivityIn(List.of(activity1, activity2, activity3))).thenReturn(Collections.emptyList());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            activityService.getAverageRatingByVolunteer(volunteerId);
        });

        assertEquals("El voluntario tiene actividades completadas, pero no hay reseñas disponibles.", exception.getMessage());
    }

    @Test
    void testUpdateActivity_Success() {
        existingActivity.setId(1);
        existingActivity.setTitle("Actividad antigua");
        updatedActivity.setTitle("Actividad actualizada");
        updatedActivity.setActivityCoordinator(2);

        when(activityRepository.findById(1)).thenReturn(Optional.of(existingActivity));
        when(activityCoordinatorService.getById(2)).thenReturn(Optional.of(new ActivityCoordinatorEntity()));
        when(activityRepository.save(existingActivity)).thenReturn(existingActivity);

        ActivityEntity result = activityService.updateActivity(1, updatedActivity);

        assertEquals("Actividad actualizada", result.getTitle());
        assertEquals(2, result.getActivityCoordinator());
        verify(activityRepository, times(1)).save(existingActivity);
    }

    @Test
    void testUpdateActivity_ActivityNotFound() {
        when(activityRepository.findById(999)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            activityService.updateActivity(999, updatedActivity);
        });

        assertEquals("La actividad con ID 999 no existe.", thrown.getMessage());
        verify(activityRepository, never()).save(any(ActivityEntity.class));
    }
}
