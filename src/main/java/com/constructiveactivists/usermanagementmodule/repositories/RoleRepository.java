package com.constructiveactivists.usermanagementmodule.repositories;

import com.constructiveactivists.usermanagementmodule.entities.RoleEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    Optional<RoleEntity> findByRoleType(RoleType roleType);
}
