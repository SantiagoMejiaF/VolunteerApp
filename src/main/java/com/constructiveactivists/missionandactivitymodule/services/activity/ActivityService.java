package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.enums.ActivityStatusEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupEntity;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupMembershipEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.MissionRepository;
import com.constructiveactivists.missionandactivitymodule.repositories.VolunteerGroupMembershipRepository;
import com.constructiveactivists.missionandactivitymodule.repositories.VolunteerGroupRepository;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupService;
import com.constructiveactivists.organizationmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import com.constructiveactivists.organizationmodule.repositories.ActivityCoordinatorRepository;
import com.constructiveactivists.missionandactivitymodule.repositories.ActivityRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.*;

@AllArgsConstructor
@Service
public class ActivityService {

    private final VolunteerGroupService volunteerGroupService;
    private final MissionRepository missionRepository;
    private final ActivityRepository activityRepository;
    private final ActivityCoordinatorRepository activityCoordinatorRepository;
    private final QRCodeService qrCodeService;
    private final ReviewEmailService reviewEmailService;
    private final VolunteerGroupMembershipRepository membershipRepository;
    private final VolunteerGroupRepository groupRepository;

    public ActivityEntity save(ActivityEntity activity) {

        MissionEntity mission = missionRepository.getMissionById(activity.getMissionId())
                .orElseThrow(() -> new EntityNotFoundException(MISSION_MEESAGE_ID + activity.getMissionId().toString() + NOT_FOUND_MESSAGE));

        Optional<ActivityCoordinatorEntity> coordinator = activityCoordinatorRepository.findById(activity.getActivityCoordinator());
        if (coordinator.isEmpty()) {
            throw new EntityNotFoundException(COORDINATOR_MESSAGE_ID + activity.getActivityCoordinator().toString() + NOT_FOUND_MESSAGE);
        }

        activity.setActivityStatus(ActivityStatusEnum.DISPONIBLE);

        VolunteerGroupEntity volunteerGroup = new VolunteerGroupEntity();
        volunteerGroup.setOrganizationId(mission.getOrganizationId());
        volunteerGroup.setName("Grupo de voluntarios para " + activity.getTitle());
        volunteerGroup.setNumberOfVolunteersRequired(activity.getNumberOfVolunteersRequired());
        volunteerGroup.setCurrentVolunteers(0);
        VolunteerGroupEntity savedVolunteerGroup = volunteerGroupService.save(volunteerGroup);

        activity.setVolunteerGroup(savedVolunteerGroup.getId());

        ActivityEntity activitySaved = activityRepository.save(activity);

        savedVolunteerGroup.setActivity(activitySaved.getId());
        volunteerGroupService.save(savedVolunteerGroup);

        activityRepository.save(activitySaved);
        reviewEmailService.sendFormEmail(activitySaved.getPersonalDataCommunityLeaderEntity().getEmailCommunityLeader(), activitySaved.getId());

        return activitySaved;
    }

    public Optional<ActivityEntity> getById(Integer id) {
        return activityRepository.findById(id);
    }

    public List<ActivityEntity> getAll() {
        return activityRepository.findAll();
    }

    public List<ActivityEntity> findAllByActivityCoordinator(Integer coordinatorId) {
        return activityRepository.findAllByActivityCoordinator(coordinatorId);
    }

    public byte[] getCheckInQrCode(Integer activityId) {
        return qrCodeService.generateCheckInQrCode(activityId);
    }

    public byte[] getCheckOutQrCode(Integer activityId) {
        return qrCodeService.generateCheckOutQrCode(activityId);
    }

    public List<ActivityEntity> getActivitiesByMissionId(Integer missionId) {
        return activityRepository.findByMissionId(missionId);
    }

    public void deleteActivityById(Integer id) {
        activityRepository.findById(id).ifPresent(activity -> {
            volunteerGroupService.deleteVolunteerGroupById(activity.getId());
            this.updateActivityStatus(id, ActivityStatusEnum.CANCELADA);
        });
    }

    public ActivityEntity updateActivityStatus(Integer id, ActivityStatusEnum newStatus) {
        Optional<ActivityEntity> activityOptional = activityRepository.findById(id);

        if (activityOptional.isPresent()) {
            ActivityEntity activity = activityOptional.get();
            activity.setActivityStatus(newStatus);
            return activityRepository.save(activity);
        } else {
            throw new EntityNotFoundException("Activity with id " + id + " not found");
        }
    }

    public List<ActivityEntity> getActivitiesByVolunteerId(Integer volunteerId) {
        List<VolunteerGroupMembershipEntity> memberships = membershipRepository.findByVolunteerId(volunteerId);
        List<Integer> groupIds = memberships.stream()
                .map(VolunteerGroupMembershipEntity::getGroupId)
                .toList();
        List<VolunteerGroupEntity> groups = groupRepository.findByIdIn(groupIds);
        List<Integer> activityIds = groups.stream()
                .map(VolunteerGroupEntity::getActivity)
                .toList();
        return activityRepository.findByIdIn(activityIds);
    }



}
