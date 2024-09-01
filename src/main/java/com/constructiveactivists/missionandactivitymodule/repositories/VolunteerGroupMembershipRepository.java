package com.constructiveactivists.missionandactivitymodule.repositories;


import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupMembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerGroupMembershipRepository  extends JpaRepository<VolunteerGroupMembershipEntity, Integer> {

    boolean existsByGroupIdAndVolunteerId(Integer groupId, Integer volunteerId);
}
