package com.constructiveactivists.organizationmodule.repositories;

import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Integer> {

    Optional<OrganizationEntity> findByUserId(Integer userId);
}
