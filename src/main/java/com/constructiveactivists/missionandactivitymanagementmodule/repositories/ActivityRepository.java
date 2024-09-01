package com.constructiveactivists.missionandactivitymanagementmodule.repositories;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.activity.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, Integer> {
    List<ActivityEntity> findAllByActivityCoordinator(Integer coordinatorId);
}
