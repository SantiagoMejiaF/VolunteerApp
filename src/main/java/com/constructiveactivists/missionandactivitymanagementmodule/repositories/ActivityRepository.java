package com.constructiveactivists.missionandactivitymanagementmodule.repositories;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.activity.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, Integer> { }
