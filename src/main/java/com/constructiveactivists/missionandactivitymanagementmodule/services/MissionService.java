package com.constructiveactivists.missionandactivitymanagementmodule.services;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.mission.enums.VisibilityEnum;
import com.constructiveactivists.missionandactivitymanagementmodule.repositories.MissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class MissionService {

    private final MissionRepository missionRepository;

    public MissionEntity save(MissionEntity mission) {

        if (mission.getOrganizationId() == null) {
            mission.setVisibility(VisibilityEnum.PUBLICA);
        }else {
            mission.setVisibility(VisibilityEnum.PRIVADA);
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
