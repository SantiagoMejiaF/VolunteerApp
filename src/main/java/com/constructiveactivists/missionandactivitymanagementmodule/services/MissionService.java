package com.constructiveactivists.missionandactivitymanagementmodule.services;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymanagementmodule.repositories.MissionRepository;
import com.constructiveactivists.organizationmanagementmodule.services.OrganizationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.constructiveactivists.authenticationmodule.controllers.configuration.constants.AppConstants.NOT_FOUND_MESSAGE;
import static com.constructiveactivists.authenticationmodule.controllers.configuration.constants.AppConstants.ORGANIZATION_MESSAGE_ID;

@AllArgsConstructor
@Service
public class MissionService {

    private final MissionRepository missionRepository;
    private final OrganizationService organizationService;

    public MissionEntity save(MissionEntity mission) {
        if (!organizationService.getOrganizationById(mission.getOrganizationId()).isPresent()) {
            throw new EntityNotFoundException(ORGANIZATION_MESSAGE_ID + mission.getOrganizationId() + NOT_FOUND_MESSAGE);
        }
        mission.setMissionStatus(MissionStatusEnum.DISPONIBLE);
        return missionRepository.save(mission);
    }

    public Optional<MissionEntity> getById(Integer id) {
        return missionRepository.findById(id);
    }

    public List<MissionEntity> getAll() {
        return missionRepository.findAll();
    }
}
