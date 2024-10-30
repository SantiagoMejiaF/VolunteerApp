package com.constructiveactivists.dashboardsandreportsmodule.services;

import com.constructiveactivists.dashboardsandreportsmodule.controllers.response.CardsOrganizationVolunteerResponse;
import com.constructiveactivists.dashboardsandreportsmodule.controllers.response.VolunteerInfoResponse;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupMembershipEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.VolunteerGroupRepository;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.repositories.OrganizationRepository;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupEntity;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupService;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import com.constructiveactivists.volunteermodule.repositories.VolunteerOrganizationRepository;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.DataShareVolunteerOrganizationService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.VolunteerOrganizationService;
import com.constructiveactivists.volunteermodule.services.volunteer.VolunteerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.*;

@Service
@AllArgsConstructor
public class DashboardVolunteerService {

    private final VolunteerService volunteerService;

    private final UserService userService;

    private final VolunteerGroupService volunteerGroupService;

    private final ActivityService activityService;

    private final VolunteerOrganizationService volunteerOrganizationService;
    private final OrganizationRepository organizationRepository;
    private final PostulationService postulationService;

    private final VolunteerGroupRepository volunteerGroupRepository;

    private final VolunteerOrganizationRepository volunteerOrganizationRepository;

    private final DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;

    public Map<String, Long> getAgeRanges() {

        return volunteerService.getAllVolunteers().stream()
                .map(volunteer -> volunteerService.calculateAge(volunteer.getPersonalInformation().getBornDate()))
                .collect(Collectors.groupingBy(age -> {
                    if (age > 100) {
                        return "100+";
                    } else {
                        int startRange = (age / 10) * 10;
                        int endRange = startRange + 9;
                        return startRange + "-" + endRange;
                    }
                }, TreeMap::new, Collectors.counting()));
    }

    public double getAverageAge() {
        return volunteerService.getAllVolunteers().stream()
                .mapToInt(volunteer -> volunteer.getPersonalInformation().getAge())
                .average()
                .orElse(0.0);
    }

    public long getActiveVolunteerCount() {
        return volunteerService.getAllVolunteers().stream()
                .filter(volunteer -> {
                    Optional<UserEntity> user = userService.getUserById(volunteer.getUserId());
                    return user.isPresent() && user.get().getAuthorizationType() == AuthorizationStatus.AUTORIZADO;
                })
                .count();
    }

    public Map<SkillEnum, Integer> getSkillCounts() {
        Map<SkillEnum, Integer> skillCountMap = new EnumMap<>(SkillEnum.class);
        volunteerService.getAllVolunteers().forEach(volunteer ->
                volunteer.getVolunteeringInformation().getSkillsList().forEach(skill ->
                        skillCountMap.merge(skill, 1, Integer::sum)
                )
        );
        for (SkillEnum skill : SkillEnum.values()) {
            skillCountMap.putIfAbsent(skill, 0);
        }
        return skillCountMap;
    }

    public Map<AvailabilityEnum, Long> getVolunteerAvailabilityCount() {
        Map<AvailabilityEnum, Long> availabilityCountMap = new EnumMap<>(AvailabilityEnum.class);
        Arrays.stream(AvailabilityEnum.values())
                .forEach(day -> availabilityCountMap.put(day, 0L));

        volunteerService.getAllVolunteers().stream()
                .flatMap(volunteer -> volunteer.getVolunteeringInformation().getAvailabilityDaysList().stream())
                .forEach(day -> availabilityCountMap.merge(day, 1L, Long::sum));

        return availabilityCountMap;
    }

    public Map<InterestEnum, Long> getInterestCount() {
        Map<InterestEnum, Long> interestCountMap = volunteerService.getAllVolunteers().stream()
                .flatMap(volunteer -> volunteer.getVolunteeringInformation().getInterestsList().stream())
                .collect(Collectors.groupingBy(
                        interest -> interest,
                        () -> new EnumMap<>(InterestEnum.class),
                        Collectors.counting()
                ));
        Arrays.stream(InterestEnum.values())
                .filter(interest -> !interestCountMap.containsKey(interest))
                .forEach(interest -> interestCountMap.put(interest, 0L));
        return interestCountMap;
    }

    public ActivityEntity getNextActivityForVolunteer(Integer volunteerId) {
        Optional <VolunteerEntity> volunteer = volunteerService.getVolunteerById(volunteerId);
        if (volunteer.isEmpty()) {
            throw new EntityNotFoundException(VOLUNTEER_MESSAGE_ID + volunteerId + NOT_FOUND_MESSAGE);
        }

        List<VolunteerOrganizationEntity> volunteerOrganizationEntities = volunteerOrganizationService.getOrganizationsByVolunteerId(volunteerId);
        if (volunteerOrganizationEntities == null || volunteerOrganizationEntities.isEmpty()) {
            return null;
        }
        Set<Integer> organizationIds = volunteerOrganizationEntities.stream()
                .map(VolunteerOrganizationEntity::getOrganizationId)
                .collect(Collectors.toSet());
        List<VolunteerGroupEntity> volunteerGroups = organizationIds.stream()
                .flatMap(orgId -> volunteerGroupService.getVolunteerGroupByOrganizationId(orgId).stream())
                .toList();
        if (volunteerGroups.isEmpty()) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        return volunteerGroups.stream()
                .map(VolunteerGroupEntity::getActivity)
                .map(activityService::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(activity -> {
                    LocalDateTime activityDateTime = LocalDateTime.of(activity.getDate(), activity.getStartTime());
                    return activityDateTime.isAfter(now);
                })
                .min(Comparator.comparing(activity -> LocalDateTime.of(activity.getDate(), activity.getStartTime())))
                .orElse(null);
    }

    public List<CardsOrganizationVolunteerResponse> getFoundationsByVolunteerId(Integer volunteerId) {
        List<VolunteerOrganizationEntity> volunteerOrganizations = volunteerOrganizationService.getOrganizationsByVolunteerId(volunteerId);
        return volunteerOrganizations.stream()
                .map(volunteerOrg -> {
                    Optional<OrganizationEntity> organization = organizationRepository.findById(volunteerOrg.getOrganizationId());

                    if (organization.isPresent()) {
                        Optional<UserEntity> userEntity = userService.getUserById(organization.get().getUserId());
                        String organizationPhoto = userEntity.map(UserEntity::getImage).orElse(null);

                        Optional<PostulationEntity> postulation = Optional.ofNullable(postulationService.findById(volunteerOrg.getId()));
                        if (postulation.map(PostulationEntity::getStatus).orElse(null) == OrganizationStatusEnum.ACEPTADO) {
                            long authorizedVolunteers = volunteerService.countAuthorizedVolunteersByOrganizationId(organization.get().getId());
                            return new CardsOrganizationVolunteerResponse(
                                    organization.get().getId(),
                                    organization.get().getOrganizationName(),
                                    organization.get().getDescription(),
                                    organization.get().getOrganizationTypeEnum(),
                                    organizationPhoto,
                                    authorizedVolunteers,
                                    organization.get().getSectorTypeEnum()
                            );
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public List<VolunteerEntity> getTenRecentVolunteers() {
        List<VolunteerEntity> volunteerEntities = volunteerService.getAllVolunteers();
        return volunteerEntities.stream()
                .sorted(Comparator.comparing(v -> v.getVolunteeringInformation().getRegistrationDate(), Comparator.reverseOrder()))
                .limit(10)
                .toList();
    }

    public Map<Month, Long> getVolunteersCountByMonth(int year) {
        LocalDateTime startDateTime = LocalDateTime.of(year, Month.JANUARY, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59, 59);
        List<VolunteerEntity> volunteers = volunteerService.getVolunteersByDateRange(startDateTime, endDateTime);
        Map<Month, Long> volunteersByMonth = new EnumMap<>(Month.class);
        for (Month month : Month.values()) {
            volunteersByMonth.put(month, 0L);
        }
        volunteers.forEach(v -> {
            Month month = v.getVolunteeringInformation().getRegistrationDate().getMonth();
            volunteersByMonth.merge(month, 1L, Long::sum);
        });
        return volunteersByMonth;
    }

    public List<VolunteerInfoResponse> getVolunteersInfoByActivityId(Integer activityId) {
        ActivityEntity activity = activityService.getById(activityId)
                .orElseThrow(() -> new EntityNotFoundException(ACTIVITY_NOT_FOUND));
        VolunteerGroupEntity volunteerGroup = volunteerGroupRepository.findById(activity.getVolunteerGroup())
                .orElseThrow(() -> new EntityNotFoundException(VOLUNTEER_GROUP_NOT_FOUND));
        Integer organizationId = volunteerGroup.getOrganizationId();
        return volunteerGroupRepository.findById(activity.getVolunteerGroup())
                .orElseThrow(() -> new EntityNotFoundException(VOLUNTEER_GROUP_NOT_FOUND))
                .getMemberships().stream()
                .map(VolunteerGroupMembershipEntity::getVolunteerId)
                .filter(volunteerId -> volunteerOrganizationRepository.existsByVolunteerIdAndOrganizationId(volunteerId, organizationId))
                .map(volunteerId -> {
                    Integer volunteerOrganizationId = volunteerOrganizationRepository
                            .findByVolunteerIdAndOrganizationId(volunteerId, organizationId)
                            .map(VolunteerOrganizationEntity::getId)
                            .orElseThrow(() -> new EntityNotFoundException(VOLUNTEER_NOT_FOUND));
                    DataShareVolunteerOrganizationEntity dataShare = dataShareVolunteerOrganizationService
                            .findById(volunteerOrganizationId);
                    VolunteerEntity volunteer = volunteerService.getVolunteerById(volunteerId)
                            .orElseThrow(() -> new EntityNotFoundException(VOLUNTEER_NOT_FOUND));
                    UserEntity user = userService.getUserById(volunteer.getUserId())
                            .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
                    VolunteerInfoResponse dto = new VolunteerInfoResponse();
                    dto.setName(user.getFirstName() + " " + user.getLastName());
                    dto.setEmail(user.getEmail());
                    dto.setIdentificationCard(volunteer.getPersonalInformation().getIdentificationCard());
                    dto.setImage(user.getImage());
                    dto.setHoursDone(dataShare.getHoursDone());
                    return dto;
                })
                .toList();
    }
}
