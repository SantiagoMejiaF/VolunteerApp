package com.constructiveactivists.missionandactivitymodule.repositories;

import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerGroupRepository extends JpaRepository<VolunteerGroupEntity, Integer> {

    List<VolunteerGroupEntity> findByOrganizationId(Integer organizationId);

    Optional<VolunteerGroupEntity> findByActivity(Integer activity);

    List<VolunteerGroupEntity> findByIdIn(List<Integer> ids);
}
