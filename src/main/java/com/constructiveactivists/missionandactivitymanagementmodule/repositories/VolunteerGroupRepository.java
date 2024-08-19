package com.constructiveactivists.missionandactivitymanagementmodule.repositories;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.VolunteerGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerGroupRepository extends JpaRepository<VolunteerGroupEntity, Integer> { }
