package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.enums.ActivityStatusEnum;
import com.constructiveactivists.missionandactivitymodule.repositories.ActivityRepository;
import com.constructiveactivists.organizationmodule.repositories.ActivityCoordinatorRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class ActivityStatusService {

    private ActivityRepository activityRepository;
    private ReviewEmailService reviewEmailService;

    private ActivityCoordinatorRepository activityCoordinatorRepository;

    private ActivityService activityService;

    @Scheduled(fixedRate = 60000)
    public void updateActivityStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<ActivityEntity> activities = activityRepository.findByActivityStatusNot(ActivityStatusEnum.COMPLETADA);
        activities.forEach(activity -> {
            LocalDateTime startDateTime = LocalDateTime.of(activity.getDate(), activity.getStartTime());
            LocalDateTime endDateTime = LocalDateTime.of(activity.getDate(), activity.getEndTime());
            if (now.isAfter(startDateTime) && now.isBefore(endDateTime)) {
                if (activity.getActivityStatus() != ActivityStatusEnum.EN_CURSO) {
                    activity.setActivityStatus(ActivityStatusEnum.EN_CURSO);
                }
            }
            else if (now.isAfter(endDateTime) && activity.getActivityStatus() == ActivityStatusEnum.EN_CURSO) {
                if (!activityService.hasVolunteers(activity.getVolunteerGroup())) {
                    activity.setActivityStatus(ActivityStatusEnum.CANCELADA);
                } else {
                    activity.setActivityStatus(ActivityStatusEnum.COMPLETADA);
                    reviewEmailService.sendFormEmail(activity.getPersonalDataCommunityLeaderEntity().getEmailCommunityLeader(), activity.getId());

                    activityCoordinatorRepository.findByUserId(activity.getActivityCoordinator())
                            .ifPresent(coordinator -> {
                                coordinator.getCompletedActivities().add(activity.getId());
                                activityCoordinatorRepository.save(coordinator);
                            });
                }
            }
        });
        if (activities.stream().anyMatch(activity -> activity.getActivityStatus() == ActivityStatusEnum.EN_CURSO || activity.getActivityStatus() == ActivityStatusEnum.COMPLETADA)) {
            activityRepository.saveAll(activities);
        }
    }
}
