package com.constructiveactivists.volunteermanagementmodule.repositories;

import com.constructiveactivists.volunteermanagementmodule.entities.volunteergroup.VolunteerGroupMembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerGroupMembershipRepository  extends JpaRepository<VolunteerGroupMembershipEntity, Integer> {
}
