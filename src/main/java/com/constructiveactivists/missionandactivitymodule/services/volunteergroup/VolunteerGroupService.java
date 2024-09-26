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

    public Optional<VolunteerGroupEntity> getVolunteerGroupById(Integer id) {
        return volunteerGroupRepository.findById(id);
    }

    public Optional<VolunteerGroupEntity> getVolunteerGroupByActivityId(Integer activityId) {
        return volunteerGroupRepository.findByActivity(activityId);
    }

    public List<VolunteerGroupEntity> getAllVolunteerGroups() {
        return volunteerGroupRepository.findAll();
    }

    public List<VolunteerGroupEntity> getVolunteerGroupByOrganizationId(Integer organizationId) {
        return volunteerGroupRepository.findByOrganizationId(organizationId);
    }

    public VolunteerGroupEntity save(VolunteerGroupEntity volunteerGroup) {
        return volunteerGroupRepository.save(volunteerGroup);
    }

    public void deleteVolunteerGroupById(Integer id) {
        volunteerGroupRepository.deleteById(id);
    }
}
