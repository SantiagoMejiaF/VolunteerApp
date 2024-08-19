package com.constructiveactivists.missionandactivitymanagementmodule.services;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.ActivityEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.enums.ActivityStatusEnum;
import com.constructiveactivists.missionandactivitymanagementmodule.repositories.ActivityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityEntity save(ActivityEntity activity) {
        activity.setActivityStatus(ActivityStatusEnum.DISPONIBLE);
        return activityRepository.save(activity);
    }

    public Optional<ActivityEntity> getById(Integer id) {
        return activityRepository.findById(id);
    }

    public List<ActivityEntity> getAll() {
        return activityRepository.findAll();
    }
}
