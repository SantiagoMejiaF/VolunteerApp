package com.constructiveactivists.missionandactivitymodule.services.volunteergroup;

import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.VolunteerGroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class VolunteerGroupService {

    private final VolunteerGroupRepository volunteerGroupRepository;

    private final VolunteerGroupMembershipService membershipService;

    public VolunteerGroupEntity save(VolunteerGroupEntity volunteerGroup) {
        VolunteerGroupEntity savedGroup = volunteerGroupRepository.save(volunteerGroup);
        savedGroup.getMemberships().forEach(membership -> membership.setGroup(savedGroup.getId()));
        membershipService.saveAll(savedGroup.getMemberships());
        return savedGroup;
    }

    public Optional<VolunteerGroupEntity> getById(Integer id) {
        return volunteerGroupRepository.findById(id);
    }

    public List<VolunteerGroupEntity> getAll() {
        return volunteerGroupRepository.findAll();
    }

    public  List<VolunteerGroupEntity> findByOrganizationId(Integer organizationId) {
        return volunteerGroupRepository.findByOrganizationId(organizationId);
    }

}
