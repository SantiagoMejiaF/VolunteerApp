package com.constructiveactivists.missionandactivitymodule.repositories;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.enums.ActivityStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, Integer> {

    List<ActivityEntity> findAllByActivityCoordinator(Integer coordinatorId);
    List<ActivityEntity> findByActivityStatusNot(ActivityStatusEnum status);
    List<ActivityEntity> findByMissionId(Integer missionId);

    List<ActivityEntity> findByIdIn(List<Integer> ids);

    List<ActivityEntity> findAllByActivityCoordinatorAndActivityStatus(Integer coordinatorId, ActivityStatusEnum activityStatus);
}
