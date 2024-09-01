package com.constructiveactivists.missionandactivitymanagementmodule.repositories;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityCoordinatorRepository extends JpaRepository<ActivityCoordinatorEntity, Integer> {
    List<ActivityCoordinatorEntity> findByOrganizationId(Integer organizationId);
}
