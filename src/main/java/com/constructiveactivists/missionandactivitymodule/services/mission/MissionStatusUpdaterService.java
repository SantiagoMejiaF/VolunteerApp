package com.constructiveactivists.missionandactivitymodule.services.mission;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.enums.ActivityStatusEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymodule.repositories.ActivityRepository;
import com.constructiveactivists.missionandactivitymodule.repositories.MissionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class MissionStatusUpdaterService {
    private MissionRepository missionRepository;
    private ActivityRepository activityRepository;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void updateMissionStatus() {
        missionRepository.findByMissionStatus(MissionStatusEnum.DISPONIBLE).stream()
                .forEach(mission -> {
                    if (LocalDate.now().isAfter(mission.getEndDate())) {
                        MissionStatusEnum newStatus = allActivitiesCompleted(mission.getId())
                                ? MissionStatusEnum.COMPLETADA
                                : MissionStatusEnum.NO_CUMPLIDA;
                        mission.setMissionStatus(newStatus);
                        missionRepository.save(mission);
                    }
                });
    }
    private boolean allActivitiesCompleted(Integer missionId) {
        List<ActivityEntity> activities = activityRepository.findByMissionId(missionId);
        return activities.stream()
                .allMatch(activity -> activity.getActivityStatus() != ActivityStatusEnum.DISPONIBLE &&
                        activity.getActivityStatus() != ActivityStatusEnum.EN_CURSO);
    }
}
