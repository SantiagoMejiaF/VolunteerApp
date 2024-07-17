package com.constructiveactivists.usermanagementmodule.repositories;

import com.constructiveactivists.usermanagementmodule.entities.volunteer.VolunteerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerRepository extends JpaRepository<VolunteerEntity, Integer> { }
