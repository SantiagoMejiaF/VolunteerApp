package com.constructiveactivists.usermanagementmodule.repositories;

import com.constructiveactivists.usermanagementmodule.entities.organization.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Integer> {


}
