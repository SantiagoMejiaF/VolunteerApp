package com.constructiveactivists.missionandactivitymodule.repositories;

import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<MissionEntity, Integer> { }
