package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.enums.ActivityStatusEnum;
import com.constructiveactivists.missionandactivitymodule.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityStatusService {

    @Autowired
    private ActivityRepository activityRepository;
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
            else if (now.isAfter(endDateTime) && activity.getActivityStatus() != ActivityStatusEnum.COMPLETADA) {
                activity.setActivityStatus(ActivityStatusEnum.COMPLETADA);
            }
        });
        if (activities.stream().anyMatch(activity -> activity.getActivityStatus() == ActivityStatusEnum.EN_CURSO || activity.getActivityStatus() == ActivityStatusEnum.COMPLETADA)) {
            activityRepository.saveAll(activities);
        }
    }
}
