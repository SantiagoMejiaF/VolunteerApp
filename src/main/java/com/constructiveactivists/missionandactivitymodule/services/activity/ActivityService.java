package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.dashboardsandreportsmodule.services.enums.Mes;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ReviewEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.enums.ActivityStatusEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupEntity;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupMembershipEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.*;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupService;
import com.constructiveactivists.organizationmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import com.constructiveactivists.organizationmodule.repositories.ActivityCoordinatorRepository;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.repositories.VolunteerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.*;

@AllArgsConstructor
@Service
public class ActivityService {

    private final VolunteerGroupService volunteerGroupService;
    private final MissionRepository missionRepository;
    private final ActivityRepository activityRepository;
    private final ActivityCoordinatorRepository activityCoordinatorRepository;
    private final QRCodeService qrCodeService;
    private final VolunteerGroupMembershipRepository membershipRepository;
    private final VolunteerGroupRepository groupRepository;
    private final ReviewRepository reviewRepository;
    private final VolunteerRepository volunteerRepository;

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

    public void updateActivityStatus(Integer id, ActivityStatusEnum newStatus) {
        Optional<ActivityEntity> activityOptional = activityRepository.findById(id);

        if (activityOptional.isPresent()) {
            ActivityEntity activity = activityOptional.get();
            activity.setActivityStatus(newStatus);
            activityRepository.save(activity);
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

    public List<ActivityEntity> getActivitiesByVolunteerAndDate(Integer volunteerId, int month, int year) {
        List<ActivityEntity> activities = this.getActivitiesByVolunteerId(volunteerId);
        return activities.stream()
                .filter(activity -> {
                    LocalDate date = activity.getDate();
                    return date.getMonthValue() == month && date.getYear() == year;
                })
                .toList();
    }

    public int getCompletedActivitiesCountVolunteer(Integer volunteerId) {
        return volunteerRepository.findById(volunteerId)
                .map(volunteer -> Optional.ofNullable(volunteer.getVolunteeringInformation())
                        .map(volunteeringInfo -> Optional.ofNullable(volunteeringInfo.getActivitiesCompleted())
                                .map(List::size)
                                .orElse(0))
                        .orElse(0))
                .orElse(0);
    }

    public int getTotalBeneficiariesImpactedByVolunteer(Integer volunteerId) {
        return volunteerRepository.findById(volunteerId)
                .map(VolunteerEntity::getVolunteeringInformation)
                .map(volunteeringInfo -> {
                    List<Integer> activitiesCompleted = volunteeringInfo.getActivitiesCompleted();
                    if (activitiesCompleted == null || activitiesCompleted.isEmpty()) {
                        return 0;
                    }
                    return activitiesCompleted.stream()
                            .map(activityRepository::findById)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .mapToInt(ActivityEntity::getNumberOfBeneficiaries)
                            .sum();
                })
                .orElse(0);
    }

    public Double getAverageRatingByVolunteer(Integer volunteerId) {
        List<ActivityEntity> completedActivities = this.getCompletedActivitiesVolunteerList(volunteerId);
        if (completedActivities.isEmpty()) {
            throw new BusinessException(VOLUNTEER_NOT_ACTIVITIES);
        }
        List<ReviewEntity> reviews = reviewRepository.findByActivityIn(completedActivities);
        if (reviews.isEmpty()) {
            throw new BusinessException(VOLUNTEER_NOT_AVAIBLE_REVIEWS);
        }
        return reviews.stream()
                .mapToInt(ReviewEntity::getRating)
                .average()
                .orElse(0.0);
    }


    public List<ActivityEntity> getCompletedActivitiesVolunteerList(Integer volunteerId) {
        return volunteerRepository.findById(volunteerId)
                .map(volunteer -> {
                    List<Integer> activityIds = Optional.ofNullable(volunteer.getVolunteeringInformation())
                            .map(volunteeringInfo -> Optional.ofNullable(volunteeringInfo.getActivitiesCompleted())
                                    .orElse(Collections.emptyList()))
                            .orElse(Collections.emptyList());
                    return activityRepository.findAllById(activityIds);
                })
                .orElse(Collections.emptyList());
    }

    public Map<String, Long> getActivitiesCountByVolunteerAndYearInSpanish(Integer volunteerId, int year) {
        List<ActivityEntity> activities = this.getActivitiesByVolunteerId(volunteerId);
        Map<String, Long> activitiesCount = Arrays.stream(Mes.values())
                .collect(Collectors.toMap(Mes::name, mes -> 0L, (e1, e2) -> e1, LinkedHashMap::new));
        activities.stream()
                .filter(activity -> activity.getDate().getYear() == year)
                .forEach(activity -> {
                    int monthIndex = activity.getDate().getMonthValue();
                    activitiesCount.put(Mes.values()[monthIndex - 1].name(), activitiesCount.get(Mes.values()[monthIndex - 1].name()) + 1);
                });
        return activitiesCount;
    }

    public List<ActivityEntity> getAvailableActivitiesByCoordinator(Integer coordinatorId) {
        return activityRepository.findAllByActivityCoordinatorAndActivityStatus(coordinatorId, ActivityStatusEnum.DISPONIBLE);
    }

    public ActivityEntity updateActivity(Integer id, ActivityEntity updatedActivity) {

        ActivityEntity existingActivity = activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("La actividad con ID " + id + " no existe."));

        existingActivity.setTitle(updatedActivity.getTitle());
        existingActivity.setDescription(updatedActivity.getDescription());
        existingActivity.setActivityCoordinator(updatedActivity.getActivityCoordinator());
        existingActivity.setDate(updatedActivity.getDate());
        existingActivity.setStartTime(updatedActivity.getStartTime());
        existingActivity.setEndTime(updatedActivity.getEndTime());
        existingActivity.setCity(updatedActivity.getCity());
        existingActivity.setLocality(updatedActivity.getLocality());
        existingActivity.setAddress(updatedActivity.getAddress());
        existingActivity.setNumberOfVolunteersRequired(updatedActivity.getNumberOfVolunteersRequired());
        existingActivity.setRequiredHours(updatedActivity.getRequiredHours());
        existingActivity.setNumberOfBeneficiaries(updatedActivity.getNumberOfBeneficiaries());
        existingActivity.setObservations(updatedActivity.getObservations());
        existingActivity.setVisibility(updatedActivity.getVisibility());

        return activityRepository.save(existingActivity);
    }

    public boolean hasVolunteers(Integer volunteerGroupId) {
        return groupRepository.findById(volunteerGroupId)
                .map(group -> !group.getMemberships().isEmpty())
                .orElse(false);
    }
}
