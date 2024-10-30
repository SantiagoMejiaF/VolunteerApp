package com.constructiveactivists.missionandactivitymodule.repositories;

import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<MissionEntity, Integer> {

    List<MissionEntity> findByOrganizationId(Integer organizationId);

    Optional<MissionEntity> getMissionById(Integer id);

    List<MissionEntity> findTop3ByOrderByCreatedAtDesc();

    List<MissionEntity> findByMissionStatus(MissionStatusEnum missionStatus);

    @Query("SELECT DISTINCT m FROM MissionEntity m " +
            "WHERE EXISTS (" +
            "   SELECT 1 FROM m.requiredInterestsList ri WHERE ri IN :interests" +
            ") OR EXISTS (" +
            "   SELECT 1 FROM m.requiredSkillsList rs WHERE rs IN :skills" +
            ")")
    List<MissionEntity> findMissionsByInterestsAndSkills(@Param("interests") List<InterestEnum> interests,
                                                         @Param("skills") List<SkillEnum> skills);
}
