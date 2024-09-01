package com.constructiveactivists.volunteermodule.repositories;

import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<VolunteerEntity, Integer> {

    Optional<VolunteerEntity> findByUserId(Integer userId);
}
