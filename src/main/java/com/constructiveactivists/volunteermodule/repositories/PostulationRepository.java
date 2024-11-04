package com.constructiveactivists.volunteermodule.repositories;

import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostulationRepository extends JpaRepository<PostulationEntity, Integer> {

    List<PostulationEntity> findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum status, List<Integer> volunteerOrganizationIds);

    List<PostulationEntity> findByVolunteerOrganizationIdIn(List<Integer> volunteerOrganizationIds);

    List<PostulationEntity> findAllByRegistrationDateBetween(LocalDate startDate, LocalDate endDate);
    @Query("SELECT vo.organizationId, COUNT(p) FROM PostulationEntity p " +
            "JOIN VolunteerOrganizationEntity vo ON p.volunteerOrganizationId = vo.id " +
            "WHERE p.status = :status " +
            "GROUP BY vo.organizationId")
    List<Object[]> countAcceptedVolunteersByOrganization(@Param("status") OrganizationStatusEnum status);
}
