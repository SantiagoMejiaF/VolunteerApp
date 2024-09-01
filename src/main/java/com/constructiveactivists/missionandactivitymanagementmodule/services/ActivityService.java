package com.constructiveactivists.missionandactivitymanagementmodule.services;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.activity.enums.ActivityStatusEnum;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.repositories.ActivityCoordinatorRepository;
import com.constructiveactivists.missionandactivitymanagementmodule.repositories.ActivityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.constructiveactivists.authenticationmodule.controllers.configuration.constants.AppConstants.*;

@AllArgsConstructor
@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final MissionService missionService;
    private final ActivityCoordinatorRepository activityCoordinatorRepository;

    public ActivityEntity save(ActivityEntity activity) {
        if (!missionService.getById(activity.getMission().getId()).isPresent()) {
            throw new EntityNotFoundException(MISSION_MEESAGE_ID + activity.getMission().getId() + NOT_FOUND_MESSAGE);
        }
        Optional<ActivityCoordinatorEntity> coordinator = activityCoordinatorRepository.findById(activity.getActivityCoordinator());
        if (coordinator.isEmpty()) {
            throw new EntityNotFoundException(COORDINATOR_MESSAGE_ID + activity.getActivityCoordinator() + NOT_FOUND_MESSAGE);
        }
        activity.setActivityStatus(ActivityStatusEnum.DISPONIBLE);
        return activityRepository.save(activity);
    }

    public Optional<ActivityEntity> getById(Integer id) {
        return activityRepository.findById(id);
    }

    public List<ActivityEntity> getAll() {
        return activityRepository.findAll();
    }

    public List<ActivityEntity> findAllByActivityCoordinator(Integer coordinatorId) {
        return activityRepository.findAllByActivityCoordinator(coordinatorId);
    }
}
