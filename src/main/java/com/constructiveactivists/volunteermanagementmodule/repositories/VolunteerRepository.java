package com.constructiveactivists.volunteermanagementmodule.repositories;

import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<VolunteerEntity, Integer> {

    Optional<VolunteerEntity> findByUserId(Integer userId);
}
