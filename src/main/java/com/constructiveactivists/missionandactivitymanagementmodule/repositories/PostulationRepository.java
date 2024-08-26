package com.constructiveactivists.missionandactivitymanagementmodule.repositories;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.postulation.PostulationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostulationRepository extends JpaRepository<PostulationEntity, Integer> { }
