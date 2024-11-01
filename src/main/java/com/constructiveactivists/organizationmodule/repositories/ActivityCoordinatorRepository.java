package com.constructiveactivists.organizationmodule.repositories;

import com.constructiveactivists.organizationmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityCoordinatorRepository extends JpaRepository<ActivityCoordinatorEntity, Integer> {

    List<ActivityCoordinatorEntity> findByOrganizationId(Integer organizationId);

    Optional<ActivityCoordinatorEntity> findByUserId(Integer userId);
}
