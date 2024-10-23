package com.constructiveactivists.volunteermodule.repositories;

import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<VolunteerEntity, Integer> {

    @Query("SELECT v FROM VolunteerEntity v WHERE v.volunteeringInformation.registrationDate >= :startDate AND v.volunteeringInformation.registrationDate < :endDate")
    List<VolunteerEntity> findByRegistrationYear(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    Optional<VolunteerEntity> findByUserId(Integer userId);

    @Query("SELECT v FROM VolunteerEntity v ORDER BY v.volunteeringInformation.registrationDate DESC")
    List<VolunteerEntity> findTop5ByOrderByRegistrationDateDesc(Pageable pageable);
}
