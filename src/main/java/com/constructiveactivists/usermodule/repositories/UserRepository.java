package com.constructiveactivists.usermodule.repositories;

import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findTop10ByAuthorizationTypeOrderByRegistrationDateDesc(AuthorizationStatus authorizationType);

    @Query("SELECT u FROM UserEntity u WHERE u.authorizationType = :authorizationStatus")
    List<UserEntity> findByAuthorizationStatus(@Param("authorizationStatus") AuthorizationStatus authorizationStatus);
}
