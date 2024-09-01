package com.constructiveactivists.postulationmanagementmodule.repositories;

import com.constructiveactivists.postulationmanagementmodule.entities.PostulationEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.OrganizationStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PostulationRepository extends JpaRepository<PostulationEntity, Integer> {
    List<PostulationEntity> findByStatus(OrganizationStatusEnum status);
    List<PostulationEntity> findByVolunteerOrganizationId(Integer volunteerOrganizationId);
    List<PostulationEntity> findAllByRegistrationDateBetween(LocalDate startDate, LocalDate endDate);

}

