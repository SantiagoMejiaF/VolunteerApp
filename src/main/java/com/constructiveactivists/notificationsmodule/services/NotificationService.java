package com.constructiveactivists.notificationsmodule.services;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.AttendanceEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.enums.ActivityStatusEnum;
import com.constructiveactivists.missionandactivitymodule.repositories.ActivityRepository;
import com.constructiveactivists.missionandactivitymodule.repositories.AttendanceRepository;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import com.constructiveactivists.missionandactivitymodule.services.mission.MissionService;
import com.constructiveactivists.notificationsmodule.entities.NotificationEntity;
import com.constructiveactivists.notificationsmodule.repositories.NotificationRepository;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.services.activitycoordinator.ActivityCoordinatorService;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.USER_NOT_FOUND;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    private final ActivityRepository activityRepository;

    private final AttendanceRepository attendanceRepository;

    private final OrganizationService organizationService;

    private final ActivityService activityService;

    private final MissionService missionService;

    private final ActivityCoordinatorService activityCoordinatorService;

    public List<NotificationEntity> getNotificationsByUserId(Integer userId) {
        return notificationRepository.findByUserId(userId);
    }

    public List<NotificationEntity> getMostRecentFiveNotificationsByUserId(Integer userId) {
        List<NotificationEntity> allNotifications = notificationRepository.findByUserId(userId);
        return allNotifications.stream()
                .sorted(Comparator.comparing(NotificationEntity::getCreationDate).reversed())
                .limit(5)
                .toList();
    }

    public void  createNotification(Integer userId, String title, String description) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException(String.format(USER_NOT_FOUND));
        }
        NotificationEntity notification = new NotificationEntity();
        notification.setTitle(title);
        notification.setDescription(description);
        notification.setCreationDate(LocalDateTime.now());
        notification.setUserId(userId);
        notificationRepository.save(notification);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void sendNotificationsVolunteerAndCoordinator() {
        LocalDateTime now = LocalDateTime.now();
        List<ActivityEntity> activities = activityRepository.findAll()
                .stream()
                .filter(activity -> activity.getActivityStatus() == ActivityStatusEnum.DISPONIBLE)
                .toList();
        activities.forEach(activity -> {
            long daysUntilStart = ChronoUnit.DAYS.between(now, activity.getDate().atStartOfDay());
            if (daysUntilStart == 7 || daysUntilStart == 3 || daysUntilStart == 1) {
                attendanceRepository.findAll()
                        .stream()
                        .filter(attendance -> attendance.getActivity().getId().equals(activity.getId()))
                        .map(AttendanceEntity::getVolunteerId)
                        .distinct()
                        .forEach(volunteerId -> createNotification(volunteerId, activity.getTitle(),
                                "Faltan " + daysUntilStart + " días para la actividad."));
            if (activity.getActivityCoordinator() != null) {
                int idActivityCoordinator = activity.getActivityCoordinator();
                activityCoordinatorService.getCoordinatorById(idActivityCoordinator)
                        .ifPresent(coordinator -> createNotification(coordinator.getUserId(), activity.getTitle(),
                                "Faltan " + daysUntilStart + " días para la actividad."));
            }
        }
        });
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void notifyUpcomingActivitiesOrganization() {
        LocalDate now = LocalDate.now();
        organizationService.getAllOrganizations().forEach(organization -> {
            List<ActivityEntity> activities = missionService.getMissionsByOrganizationId(organization.getId()).stream()
                    .flatMap(mission -> activityService.getActivitiesByMissionId(mission.getId()).stream())
                    .filter(activity -> activity.getActivityStatus() == ActivityStatusEnum.DISPONIBLE)
                    .toList();

            activities.forEach(activity -> {
                long daysUntilActivity = ChronoUnit.DAYS.between(now, activity.getDate());

                // Using if statements instead of switch
                if (daysUntilActivity == 14) {
                    createNotificationForOrganization(organization.getUserId(), activity, "Faltan 2 semanas para la actividad: " + activity.getTitle());
                } else if (daysUntilActivity == 7) {
                    createNotificationForOrganization(organization.getUserId(), activity, "Falta 1 semana para la actividad: " + activity.getTitle());
                } else if (daysUntilActivity == 3) {
                    createNotificationForOrganization(organization.getUserId(), activity, "Faltan 3 días para la actividad: " + activity.getTitle());
                } else if (daysUntilActivity == 1) {
                    createNotificationForOrganization(organization.getUserId(), activity, "Falta 1 día para la actividad: " + activity.getTitle());
                }
            });
        });
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void checkActivityRegistration() {
        LocalDate now = LocalDate.now();
        organizationService.getAllOrganizations().forEach(organization -> {
            List<ActivityEntity> activities = missionService.getMissionsByOrganizationId(organization.getId()).stream()
                    .flatMap(mission -> activityService.getActivitiesByMissionId(mission.getId()).stream())
                    .filter(activity -> activity.getActivityStatus() == ActivityStatusEnum.DISPONIBLE)
                    .toList();
            activities.forEach(activity -> {
                long daysUntilActivity = ChronoUnit.DAYS.between(now, activity.getDate());
                if (daysUntilActivity == 7) {
                    checkVolunteerCountAndNotify(organization, activity);
                }
            });
        });
    }

    private void checkVolunteerCountAndNotify(OrganizationEntity organization, ActivityEntity activity) {
        int requiredVolunteers = activity.getNumberOfVolunteersRequired();
        int registeredVolunteers = activity.getAttendances().size();

        String message;
        if (registeredVolunteers < requiredVolunteers) {
            message = String.format("¡Atención! La actividad \"%s\" requiere %d voluntarios, pero solo se han registrado %d. ¡Necesitamos más apoyo!",
                    activity.getTitle(), requiredVolunteers, registeredVolunteers);
        } else {
            message = String.format("¡Genial! Todos los %d voluntarios necesarios para la actividad \"%s\" ya están registrados. ¡Gracias por su compromiso!",
                    requiredVolunteers, activity.getTitle());
        }
        createNotificationForOrganization(organization.getId(), activity, message);
    }
    private void createNotificationForOrganization(Integer organizationId, ActivityEntity activity, String message) {
        this.createNotification(organizationId, activity.getTitle(), message);
    }
}
