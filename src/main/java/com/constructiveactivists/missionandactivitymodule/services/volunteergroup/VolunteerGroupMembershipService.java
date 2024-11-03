package com.constructiveactivists.missionandactivitymodule.services.volunteergroup;

import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupMembershipEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.VolunteerGroupMembershipRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VolunteerGroupMembershipService {

    private final VolunteerGroupMembershipRepository volunteerGroupMembershipRepository;

    public boolean isVolunteerInGroup(Integer groupId, Integer volunteerId) {
        return volunteerGroupMembershipRepository.existsByGroupIdAndVolunteerId(groupId, volunteerId);
    }

    public void addVolunteerToGroup(Integer groupId, Integer volunteerId) {
        VolunteerGroupMembershipEntity membership = new VolunteerGroupMembershipEntity();
        membership.setGroupId(groupId);
        membership.setVolunteerId(volunteerId);
        volunteerGroupMembershipRepository.save(membership);
    }

    @Transactional
    public void removeVolunteerFromGroup(Integer groupId, Integer volunteerId) {
        volunteerGroupMembershipRepository.deleteByGroupIdAndVolunteerId(groupId, volunteerId);
    }

}
