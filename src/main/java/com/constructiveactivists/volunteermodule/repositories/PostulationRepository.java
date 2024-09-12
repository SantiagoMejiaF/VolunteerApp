package com.constructiveactivists.volunteermodule.repositories;

import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PostulationRepository extends JpaRepository<PostulationEntity, Integer> {
    List<PostulationEntity> findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum status, List<Integer> volunteerOrganizationIds);
    List<PostulationEntity> findByVolunteerOrganizationId(Integer volunteerOrganizationId);
    List<PostulationEntity> findAllByRegistrationDateBetween(LocalDate startDate, LocalDate endDate);

}
