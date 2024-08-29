package com.constructiveactivists.volunteermanagementmodule.repositories;

import com.constructiveactivists.volunteermanagementmodule.entities.volunteergroup.VolunteerGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolunteerGroupRepository extends JpaRepository<VolunteerGroupEntity, Integer> {
    List<VolunteerGroupEntity> findByOrganizationId(Integer organizationId);
}
