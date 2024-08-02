package com.constructiveactivists.usermanagementmodule.repositories;


import com.constructiveactivists.usermanagementmodule.entities.user.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByNameRole(String nameRole);
}
