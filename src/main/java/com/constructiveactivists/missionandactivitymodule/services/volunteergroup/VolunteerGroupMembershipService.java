package com.constructiveactivists.missionandactivitymodule.services.volunteergroup;

import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupMembershipEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.VolunteerGroupMembershipRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VolunteerGroupMembershipService {

    private VolunteerGroupMembershipRepository membershipRepository;

    public List<VolunteerGroupMembershipEntity> saveAll(List<VolunteerGroupMembershipEntity> memberships) {
        return membershipRepository.saveAll(memberships);
    }

}
