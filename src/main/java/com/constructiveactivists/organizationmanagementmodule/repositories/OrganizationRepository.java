package com.constructiveactivists.organizationmanagementmodule.repositories;

import com.constructiveactivists.organizationmanagementmodule.entities.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Integer> {


}
