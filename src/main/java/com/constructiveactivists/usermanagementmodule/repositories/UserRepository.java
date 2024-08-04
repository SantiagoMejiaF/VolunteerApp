package com.constructiveactivists.usermanagementmodule.repositories;

import com.constructiveactivists.usermanagementmodule.entities.user.RoleEntity;
import com.constructiveactivists.usermanagementmodule.entities.user.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);
    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.role.id = :roleId WHERE u.id = :userId")
    void updateRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);





}
