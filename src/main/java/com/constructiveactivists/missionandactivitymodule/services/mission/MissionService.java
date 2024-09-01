package com.constructiveactivists.missionandactivitymodule.services.mission;

import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymodule.repositories.MissionRepository;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.NOT_FOUND_MESSAGE;
import static com.constructiveactivists.configurationmodule.constants.AppConstants.ORGANIZATION_MESSAGE_ID;

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

    public Optional<MissionEntity> getMissionById(Integer id) {
        return missionRepository.findById(id);
    }

    public List<MissionEntity> getAllMisions() {
        return missionRepository.findAll();
    }
}
