package com.constructiveactivists.missionandactivitymodule.repositories;


import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupMembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerGroupMembershipRepository  extends JpaRepository<VolunteerGroupMembershipEntity, Integer> {

    boolean existsByGroupIdAndVolunteerId(Integer groupId, Integer volunteerId);

    List<VolunteerGroupMembershipEntity> findByVolunteerId(Integer volunteerId);

    void deleteByGroupIdAndVolunteerId(Integer groupId, Integer volunteerId);
}
