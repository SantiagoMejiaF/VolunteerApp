package com.constructiveactivists.missionandactivitymodule.services.mission;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionTypeEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VolunteerMissionRequirementsEnum;
import com.constructiveactivists.missionandactivitymodule.repositories.MissionRepository;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
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
    private final ActivityService activityService; // ajuste

    public MissionEntity save(MissionEntity mission) {
        if (organizationService.getOrganizationById(mission.getOrganizationId()).isEmpty()) {
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

    public List<InterestEnum> getRequiredInterests() {
        return Arrays.asList(InterestEnum.values());
    }

    public void cancelMissionById(Integer missionId) {
        Optional<MissionEntity> missionOptional = missionRepository.findById(missionId);
        if (missionOptional.isPresent()) {
            MissionEntity mission = missionOptional.get();
            mission.setMissionStatus(MissionStatusEnum.CANCELADA);
            missionRepository.save(mission);
            List<ActivityEntity> activities = activityService.getActivitiesByMissionId(missionId);
            activities.forEach(activity -> activityService.deleteActivityById(activity.getId()));
        } else {
            throw new EntityNotFoundException("Mission with ID " + missionId + " not found");
        }
    }

    public List<ActivityEntity> getActivitiesByMissionId(Integer missionId) {
        Optional<MissionEntity> mission = missionRepository.findById(missionId);
        if (mission.isEmpty()) {
            throw new EntityNotFoundException("Mission with ID " + missionId + " not found");
        }
        return activityService.getActivitiesByMissionId(missionId);
    }

    public List<ActivityEntity> getAllActivitiesByOrganizationId(Integer organizationId) {
        return missionRepository.findByOrganizationId(organizationId).stream()
                .flatMap(mission -> activityService.getActivitiesByMissionId(mission.getId()).stream())
                .toList();
    }

    public List<MissionEntity> getLastThreeMissions() {
        return missionRepository.findTop3ByOrderByCreatedAtDesc();
    }

    public MissionEntity updateMission(Integer missionId, MissionEntity updatedMissionData) {
        MissionEntity existingMission = missionRepository.findById(missionId)
                .orElseThrow(() -> new EntityNotFoundException("Mission with ID " + missionId + " not found"));

        existingMission.setMissionType(updatedMissionData.getMissionType());
        existingMission.setTitle(updatedMissionData.getTitle());
        existingMission.setDescription(updatedMissionData.getDescription());
        existingMission.setStartDate(updatedMissionData.getStartDate());
        existingMission.setEndDate(updatedMissionData.getEndDate());
        existingMission.setDepartment(updatedMissionData.getDepartment());
        existingMission.setVolunteerMissionRequirementsEnumList(updatedMissionData.getVolunteerMissionRequirementsEnumList());
        existingMission.setRequiredSkillsList(updatedMissionData.getRequiredSkillsList());
        existingMission.setVisibility(updatedMissionData.getVisibility());

        return missionRepository.save(existingMission);
    }
}
