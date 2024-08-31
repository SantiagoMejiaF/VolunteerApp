package com.constructiveactivists.volunteermanagementmodule.repositories;

import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.OrganizationStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerOrganizationRepository extends JpaRepository<VolunteerOrganizationEntity, Integer> {

    List<VolunteerOrganizationEntity> findByVolunteerId(Integer volunteerId);

    List<VolunteerOrganizationEntity> findByOrganizationId(Integer organizationId);

    boolean existsByVolunteerIdAndOrganizationId(Integer volunteerId, Integer organizationId);

    List<VolunteerOrganizationEntity> findByStatus(OrganizationStatusEnum status);
    List<VolunteerOrganizationEntity> findAll();
    Optional<VolunteerOrganizationEntity> findByVolunteerIdAndOrganizationId(Integer volunteerId, Integer organizationId);
}

