package com.constructiveactivists.notificationsmodule.services;

import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import com.constructiveactivists.missionandactivitymodule.services.mission.MissionService;
import com.constructiveactivists.notificationsmodule.entities.NotificationEntity;
import com.constructiveactivists.notificationsmodule.repositories.NotificationRepository;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.enums.ActivityStatusEnum;
import com.constructiveactivists.missionandactivitymodule.repositories.ActivityRepository;
import com.constructiveactivists.missionandactivitymodule.repositories.AttendanceRepository;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ActivityService activityService;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private MissionService missionService;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getNotificationsByUserId_ShouldReturnNotifications() {
        Integer userId = 1;
        List<NotificationEntity> notifications = List.of(new NotificationEntity());

        when(notificationRepository.findByUserId(userId)).thenReturn(notifications);

        List<NotificationEntity> result = notificationService.getNotificationsByUserId(userId);

        assertEquals(notifications, result);
        verify(notificationRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getMostRecentFiveNotificationsByUserId_ShouldReturnFiveMostRecentNotifications() {
        Integer userId = 1;
        NotificationEntity notification1 = new NotificationEntity();
        notification1.setCreationDate(LocalDateTime.now().minusDays(5));
        NotificationEntity notification2 = new NotificationEntity();
        notification2.setCreationDate(LocalDateTime.now().minusDays(3));

        List<NotificationEntity> notifications = List.of(notification1, notification2);

        when(notificationRepository.findByUserId(userId)).thenReturn(notifications);

        List<NotificationEntity> result = notificationService.getMostRecentFiveNotificationsByUserId(userId);

        assertEquals(2, result.size());
        assertEquals(notification2, result.get(0));
        verify(notificationRepository, times(1)).findByUserId(userId);
    }

    @Test
    void createNotification_ShouldCreateNotificationForExistingUser() {
        Integer userId = 1;
        String title = "Test Title";
        String description = "Test Description";

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));

        notificationService.createNotification(userId, title, description);

        verify(notificationRepository, times(1)).save(any(NotificationEntity.class));
    }

    @Test
    void createNotification_UserNotFound_ShouldThrowException() {
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                notificationService.createNotification(userId, "Title", "Description"));
    }

    @Test
    void sendNotificationsVolunteerAndCoordinator_ShouldNotifyVolunteersAndCoordinators() {
        ActivityEntity activity = new ActivityEntity();
        activity.setId(1);
        activity.setDate(LocalDate.now().plusDays(7));
        activity.setActivityStatus(ActivityStatusEnum.DISPONIBLE);

        when(activityRepository.findAll()).thenReturn(List.of(activity));
        when(attendanceRepository.findAll()).thenReturn(Collections.emptyList());

        notificationService.sendNotificationsVolunteerAndCoordinator();

        verify(notificationRepository, never()).save(any(NotificationEntity.class));
    }

    @Test
    void notifyUpcomingActivitiesOrganization_ShouldNotifyOrganizations() {
        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(1);
        organization.setUserId(1);

        UserEntity user = new UserEntity();
        user.setId(1);

        MissionEntity mission = new MissionEntity();
        mission.setId(1);

        ActivityEntity activity = new ActivityEntity();
        activity.setId(1);
        activity.setDate(LocalDate.now().plusDays(14));
        activity.setActivityStatus(ActivityStatusEnum.DISPONIBLE);

        when(organizationService.getAllOrganizations()).thenReturn(List.of(organization));
        when(missionService.getMissionsByOrganizationId(organization.getId())).thenReturn(List.of(mission));
        when(activityService.getActivitiesByMissionId(mission.getId())).thenReturn(List.of(activity));
        when(userRepository.findById(organization.getUserId())).thenReturn(Optional.of(user));

        notificationService.notifyUpcomingActivitiesOrganization();

        verify(notificationRepository, times(1)).save(any(NotificationEntity.class));
    }

    @Test
    void checkActivityRegistration_ShouldNotifyWhenVolunteerCountIsInsufficient() {
        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(1);
        organization.setUserId(1);

        UserEntity user = new UserEntity();
        user.setId(1);

        MissionEntity mission = new MissionEntity();
        mission.setId(1);

        ActivityEntity activity = new ActivityEntity();
        activity.setId(1);
        activity.setDate(LocalDate.now().plusDays(7));
        activity.setActivityStatus(ActivityStatusEnum.DISPONIBLE);
        activity.setNumberOfVolunteersRequired(10);
        activity.setAttendances(Collections.emptyList());

        when(organizationService.getAllOrganizations()).thenReturn(List.of(organization));
        when(missionService.getMissionsByOrganizationId(organization.getId())).thenReturn(List.of(mission));
        when(activityService.getActivitiesByMissionId(mission.getId())).thenReturn(List.of(activity));
        when(userRepository.findById(organization.getUserId())).thenReturn(Optional.of(user));

        notificationService.checkActivityRegistration();

        verify(notificationRepository, times(1)).save(any(NotificationEntity.class));
    }
}
