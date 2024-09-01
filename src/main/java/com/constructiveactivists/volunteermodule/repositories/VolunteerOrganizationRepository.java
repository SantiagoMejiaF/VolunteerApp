package com.constructiveactivists.volunteermodule.repositories;

import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerOrganizationRepository extends JpaRepository<VolunteerOrganizationEntity, Integer> {

    List<VolunteerOrganizationEntity> findByVolunteerId(Integer volunteerId);

    List<VolunteerOrganizationEntity> findByOrganizationId(Integer organizationId);

    boolean existsByVolunteerIdAndOrganizationId(Integer volunteerId, Integer organizationId);

    List<VolunteerOrganizationEntity> findAll();

    Optional<VolunteerOrganizationEntity> findByVolunteerIdAndOrganizationId(Integer volunteerId, Integer organizationId);
}
