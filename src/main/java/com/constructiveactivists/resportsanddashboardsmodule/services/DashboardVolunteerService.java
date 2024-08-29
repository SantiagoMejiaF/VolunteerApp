package com.constructiveactivists.resportsanddashboardsmodule.services;

import com.constructiveactivists.authenticationmodule.controllers.configuration.exceptions.BusinessException;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.activity.ActivityEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.volunteergroup.VolunteerGroupEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.services.ActivityService;
import com.constructiveactivists.volunteermanagementmodule.services.VolunteerGroupService;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.InterestEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.SkillEnum;
import com.constructiveactivists.volunteermanagementmodule.services.VolunteerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DashboardVolunteerService {

    private final VolunteerService volunteerService;

    private final UserService userService;

    private final VolunteerGroupService volunteerGroupService;

    private final ActivityService activityService;

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
        VolunteerEntity volunteer = volunteerService.getVolunteerById(volunteerId)
                .orElseThrow(() -> new BusinessException("Voluntario no encontrado"));
        Integer organizationId = volunteer.getOrganizationId();
        List<VolunteerGroupEntity> volunteerGroups = volunteerGroupService.findByOrganizationId(organizationId);
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
}
