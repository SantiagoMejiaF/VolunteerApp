package com.constructiveactivists.usermanagementmodule.repositories;

import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.AuthorizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);

    @Query(value = "SELECT COUNT(*) FROM user_management_module.USUARIO u " +
            "JOIN user_management_module.ROLE r ON u.ROL_ID = r.ID " +
            "WHERE r.TIPO_ROL = 'ORGANIZACION' AND u.ESTADO_AUTORIZACION = :authorizationStatus",
            nativeQuery = true)
    long countOrganizationsByStatus(@Param("authorizationStatus") String authorizationStatus);

    @Query("SELECT u FROM UserEntity u WHERE u.authorizationType = :authorizationStatus")
    List<UserEntity> findByAuthorizationStatus(@Param("authorizationStatus") AuthorizationStatus authorizationStatus);
}
