package com.constructiveactivists.postulationmanagementmodule.repositories;

import com.constructiveactivists.postulationmanagementmodule.entities.DataShareVolunteerOrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataShareVolunteerOrganizationRepository extends JpaRepository<DataShareVolunteerOrganizationEntity, Integer> {
    List<DataShareVolunteerOrganizationEntity> findAllByVolunteerOrganizationIdIn(List<Integer> ids);
}

