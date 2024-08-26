package com.constructiveactivists.missionandactivitymanagementmodule.services;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.volunteergroup.VolunteerGroupEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.repositories.VolunteerGroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class VolunteerGroupService {

    private final VolunteerGroupRepository volunteerGroupRepository;

    public VolunteerGroupEntity save(VolunteerGroupEntity volunteerGroup) {
        return volunteerGroupRepository.save(volunteerGroup);
    }

    public Optional<VolunteerGroupEntity> getById(Integer id) {
        return volunteerGroupRepository.findById(id);
    }

    public List<VolunteerGroupEntity> getAll() {
        return volunteerGroupRepository.findAll();
    }
}
