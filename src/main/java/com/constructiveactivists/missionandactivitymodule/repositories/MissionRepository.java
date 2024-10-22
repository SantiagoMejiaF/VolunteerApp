package com.constructiveactivists.missionandactivitymodule.repositories;

import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<MissionEntity, Integer> {

    List<MissionEntity> findByOrganizationId(Integer organizationId);

    Optional<MissionEntity> getMissionById(Integer id);

    @Query("SELECT m FROM MissionEntity m ORDER BY m.createdAt DESC")
    List<MissionEntity> findTop3ByOrderByCreatedAtDesc();
}
