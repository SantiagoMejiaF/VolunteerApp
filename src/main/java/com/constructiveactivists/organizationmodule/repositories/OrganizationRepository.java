package com.constructiveactivists.organizationmodule.repositories;

import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Integer> {


    @Query("SELECT o FROM OrganizationEntity o WHERE o.registrationDate >= :startDate AND o.registrationDate < :endDate")
    List<OrganizationEntity> findByRegistrationYear(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    Optional<OrganizationEntity> findByUserId(Integer userId);
    List<OrganizationEntity> findTop10ByOrderByRegistrationDateDesc();
}
