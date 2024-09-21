package com.constructiveactivists.missionandactivitymodule.services.mission;

import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionTypeEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VolunteerMissionRequirementsEnum;
import com.constructiveactivists.missionandactivitymodule.repositories.MissionRepository;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    public List<MissionEntity> getMissionsByOrganizationId(Integer organizationId) {
        return missionRepository.findByOrganizationId(organizationId);
    }

    public List<MissionTypeEnum> getMissionTypes() {
        return Arrays.asList(MissionTypeEnum.values());
    }

    public List<VisibilityEnum> getVisibilityOptions() {
        return Arrays.asList(VisibilityEnum.values());
    }

    public List<MissionStatusEnum> getMissionStatusOptions() {
        return Arrays.asList(MissionStatusEnum.values());
    }

    public List<VolunteerMissionRequirementsEnum> getVolunteerRequirements() {
        return Arrays.asList(VolunteerMissionRequirementsEnum.values());
    }

    public List<SkillEnum> getRequiredSkills() {
        return Arrays.asList(SkillEnum.values());
    }

}
