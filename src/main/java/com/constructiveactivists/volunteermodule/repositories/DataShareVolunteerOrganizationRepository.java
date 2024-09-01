package com.constructiveactivists.volunteermodule.repositories;

import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataShareVolunteerOrganizationRepository extends JpaRepository<DataShareVolunteerOrganizationEntity, Integer> {
    List<DataShareVolunteerOrganizationEntity> findAllByVolunteerOrganizationIdIn(List<Integer> ids);
}

