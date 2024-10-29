package com.constructiveactivists.dashboardsandreportsmodule.services;

import com.constructiveactivists.dashboardsandreportsmodule.controllers.response.CardsOrganizationVolunteerResponse;
import com.constructiveactivists.dashboardsandreportsmodule.services.enums.Mes;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ReviewEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.enums.ActivityStatusEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymodule.repositories.ActivityRepository;
import com.constructiveactivists.missionandactivitymodule.repositories.MissionRepository;
import com.constructiveactivists.missionandactivitymodule.repositories.ReviewRepository;
import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.missionandactivitymodule.services.mission.MissionService;
import com.constructiveactivists.organizationmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.repositories.ActivityCoordinatorRepository;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.repositories.VolunteerRepository;
import com.constructiveactivists.volunteermodule.services.volunteer.VolunteerService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.DataShareVolunteerOrganizationService;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.VolunteerOrganizationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.*;

@Service
@AllArgsConstructor
public class DashboardOrganizationService {

    private final OrganizationService organizationService;
    private final UserService userService;
    private final MissionService missionService;
    private final PostulationService postulationService;
    private final VolunteerOrganizationService volunteerOrganizationService;
    private final DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;

    private final VolunteerService volunteerService;

    private final VolunteerRepository volunteerRepository;

    private final ActivityRepository activityRepository;

    private final MissionRepository missionRepository;

    private final ReviewRepository reviewRepository;

    private final ActivityCoordinatorRepository activityCoordinatorRepository;

    private void getOrganizationById(Integer organizationId) {
        Optional<OrganizationEntity> organization = organizationService.getOrganizationById(organizationId);
        if (organization.isEmpty()) {
            throw new EntityNotFoundException(ORGANIZATION_MESSAGE_ID + organizationId + NOT_FOUND_MESSAGE);
        }
    }

    private List<DataShareVolunteerOrganizationEntity> getDataSharesByOrganizationId(Integer organizationId) {
        List<Integer> volunteerOrganizationIds = volunteerOrganizationService.findVolunteerOrganizationIdsByOrganizationId(organizationId);
        return dataShareVolunteerOrganizationService.findAllByVolunteerOrganizationIdIn(volunteerOrganizationIds);
    }

    public long getActiveOrganizationCount() {
        return organizationService.getAllOrganizations().stream()
                .filter(organization -> {
                    Optional<UserEntity> user = userService.getUserById(organization.getUserId());
                    return user.isPresent() && user.get().getAuthorizationType() == AuthorizationStatus.AUTORIZADO;
                })
                .count();
    }

    public long countCompleteMissions() {
        return missionService.getAllMisions().stream()
                .filter(mission -> mission.getMissionStatus() == MissionStatusEnum.COMPLETADA)
                .count();
    }

    public long countCompleteMissionsByOrganization(Integer organizationId) {
        return missionService.getAllMisions().stream()
                .filter(mission -> mission.getMissionStatus() == MissionStatusEnum.COMPLETADA
                        && mission.getOrganizationId().equals(organizationId))
                .count();
    }

    public Double getAverageHoursForOrganization(Integer organizationId) {
        getOrganizationById(organizationId);
        List<DataShareVolunteerOrganizationEntity> dataShares = getDataSharesByOrganizationId(organizationId);
        return dataShares.stream()
                .collect(Collectors.averagingDouble(data -> data.getHoursDone() - data.getHoursCertified()));
    }

    public Double getAverageMonthlyHoursByOrganization(Integer organizationId) {
        getOrganizationById(organizationId);
        List<DataShareVolunteerOrganizationEntity> dataShares = getDataSharesByOrganizationId(organizationId);
        return dataShares.stream()
                .filter(data -> data.getMonthlyHours() != null && data.getMonthlyHours() > 0)
                .collect(Collectors.averagingDouble(DataShareVolunteerOrganizationEntity::getMonthlyHours));
    }
    public List<VolunteerOrganizationEntity> getVolunteersByOrganizationAndMonth(Integer organizationId, Integer month, Integer year) {
        getOrganizationById(organizationId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List<VolunteerOrganizationEntity> volunteerOrganizations = volunteerOrganizationService.getVolunteersByOrganizationId(organizationId);
        List<PostulationEntity> postulations = postulationService.getPostulationsByDateRange(startDate, endDate);
        Set<Integer> postulatedVolunteerIds = postulations.stream()
                .map(PostulationEntity::getVolunteerOrganizationId)
                .collect(Collectors.toSet());
        return volunteerOrganizations.stream()
                .filter(volunteerOrg -> postulatedVolunteerIds.contains(volunteerOrg.getId()))
                .toList();
    }

    public List<OrganizationEntity> getTenRecentOrganizations() {
        return organizationService.getTenRecentOrganizations();
    }

    public Map<Month, Long> getOrganizationsCountByMonth(int year) {
        LocalDateTime startDateTime = LocalDateTime.of(year, Month.JANUARY, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59, 59);
        List<OrganizationEntity> organizations = organizationService.getOrganizationsByDateRange(startDateTime, endDateTime);
        Map<Month, Long> organizationsByMonth = new EnumMap<>(Month.class);
        for (Month month : Month.values()) {
            organizationsByMonth.put(month, 0L);
        }
        organizations.forEach(o -> {
            Month month = o.getRegistrationDate().getMonth();
            organizationsByMonth.merge(month, 1L, Long::sum);
        });
        return organizationsByMonth;
    }

    public Map<SkillEnum, Integer> getSkillCountsByOrganization(Integer organizationId) {
        Map<SkillEnum, Integer> skillCountMap = new EnumMap<>(SkillEnum.class);
        List<VolunteerOrganizationEntity> acceptedVolunteerOrganizations = volunteerOrganizationService.findAcceptedVolunteerOrganizationsByOrganizationId(organizationId);
        acceptedVolunteerOrganizations.forEach(volunteerOrgEntity -> {
            VolunteerEntity volunteer = volunteerService.getVolunteerById(volunteerOrgEntity.getVolunteerId())
                    .orElseThrow(() -> new BusinessException(VOLUNTEER_NOT_FOUND));
            volunteer.getVolunteeringInformation().getSkillsList().forEach(skill ->
                    skillCountMap.merge(skill, 1, Integer::sum)
            );
        });
        for (SkillEnum skill : SkillEnum.values()) {
            skillCountMap.putIfAbsent(skill, 0);
        }
        return skillCountMap;
    }

    public Map<AvailabilityEnum, Long> getVolunteerAvailabilityCountByOrganization(Integer organizationId) {
        Map<AvailabilityEnum, Long> availabilityCountMap = new EnumMap<>(AvailabilityEnum.class);
        Arrays.stream(AvailabilityEnum.values())
                .forEach(day -> availabilityCountMap.put(day, 0L));
        List<VolunteerOrganizationEntity> acceptedVolunteerOrganizations = volunteerOrganizationService
                .findAcceptedVolunteerOrganizationsByOrganizationId(organizationId);
        List<Integer> acceptedVolunteerIds = acceptedVolunteerOrganizations.stream()
                .map(VolunteerOrganizationEntity::getVolunteerId)
                .filter(Objects::nonNull)
                .toList();
        List<VolunteerEntity> acceptedVolunteers = acceptedVolunteerIds.stream()
                .map(volunteerService::getVolunteerById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        acceptedVolunteers.stream()
                .flatMap(volunteer -> volunteer.getVolunteeringInformation().getAvailabilityDaysList().stream())
                .forEach(day -> availabilityCountMap.merge(day, 1L, Long::sum));
        return availabilityCountMap;
    }

    public int getTotalBeneficiariesImpactedByOrganization(Integer organizationId) {
        List<MissionEntity> missions = missionService.getMissionsByOrganizationId(organizationId);
        Set<Integer> uniqueBeneficiaries = new HashSet<>();
        missions.stream()
                .flatMap(mission -> missionService.getActivitiesByMissionId(mission.getId()).stream())
                .filter(activity -> ActivityStatusEnum.COMPLETADA.equals(activity.getActivityStatus()))
                .map(ActivityEntity::getNumberOfBeneficiaries)
                .forEach(uniqueBeneficiaries::add);
        return uniqueBeneficiaries.stream().mapToInt(Integer::intValue).sum();
    }

    public List<ReviewEntity> getReviewsByOrganization(Integer organizationId) {
        return missionRepository.findByOrganizationId(organizationId).stream()
                .flatMap(mission -> activityRepository.findByMissionId(mission.getId()).stream())
                .flatMap(activity -> reviewRepository.findByActivity(activity).stream())
                .toList();
    }

    public List<ReviewEntity> getCoordinatorReviewHistory(Integer userId) {
        return activityCoordinatorRepository.findById(userId)
                .map(ActivityCoordinatorEntity::getCompletedActivities)
                .map(completedActivityIds -> {
                    List<ActivityEntity> activities = activityRepository.findByIdIn(completedActivityIds);
                    return reviewRepository.findByActivityIn(activities);
                })
                .orElse(Collections.emptyList());
    }

    public Map<String, Long> getActivitiesCountByOrganizationAndYear(Integer organizationId, int year) {
        List<MissionEntity> missions = missionService.getMissionsByOrganizationId(organizationId);
        Map<String, Long> activitiesCount = Arrays.stream(Mes.values())
                .collect(Collectors.toMap(Mes::name, mes -> 0L, (e1, e2) -> e1, LinkedHashMap::new));
        missions.stream()
                .flatMap(mission -> activityRepository.findByMissionId(mission.getId()).stream()
                        .filter(activity -> activity.getDate().getYear() == year)
                        .map(activity -> Mes.values()[activity.getDate().getMonthValue() - 1]))
                .forEach(month -> activitiesCount.merge(String.valueOf(month), 1L, Long::sum));

        return activitiesCount;
    }

    public List<CardsOrganizationVolunteerResponse> getAllOrganizationsCards() {
        return organizationService.getAllOrganizations().stream()
                .map(organization -> {
                    Optional<UserEntity> userEntity = userService.getUserById(organization.getUserId());
                    String organizationPhoto = userEntity.map(UserEntity::getImage).orElse(null);
                    long authorizedVolunteers = volunteerService.countAuthorizedVolunteersByOrganizationId(organization.getId());
                    return new CardsOrganizationVolunteerResponse(
                            organization.getId(),
                            organization.getOrganizationName(),
                            organization.getDescription(),
                            organization.getOrganizationTypeEnum(),
                            organizationPhoto,
                            authorizedVolunteers,
                            organization.getSectorTypeEnum()
                    );
                })
                .filter(Objects::nonNull)
                .toList();
    }


}
