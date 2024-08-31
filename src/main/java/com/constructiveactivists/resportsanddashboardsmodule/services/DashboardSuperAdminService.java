package com.constructiveactivists.resportsanddashboardsmodule.services;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.services.ActivityService;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermanagementmodule.services.VolunteerOrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DashboardSuperAdminService {

    private final ActivityService activityService;
    private final VolunteerOrganizationService volunteerOrganizationService;

    public Map<String, Long> getActivityLocalitiesWithFrequency() {
        List<ActivityEntity> activities = activityService.getAll();
        return activities.stream()
                .collect(Collectors.groupingBy(ActivityEntity::getLocality, Collectors.counting()));
    }


    public double getAverageHours() {
        List<VolunteerOrganizationEntity> entities = volunteerOrganizationService.findAll();
        return entities.stream()
                .mapToDouble(entity -> entity.getHoursDone() - entity.getHoursCertified())
                .average()
                .orElse(0.0);
    }

    public Double getAverageMonthlyHours() {
        List<VolunteerOrganizationEntity> entities = volunteerOrganizationService.findAll();
        return entities.stream()
                .filter(entity -> entity.getMonthlyHours() != null && entity.getMonthlyHours() > 0)
                .collect(Collectors.averagingDouble(VolunteerOrganizationEntity::getMonthlyHours));
    }

    public List<VolunteerOrganizationEntity> getRecentVolunteersByWeek() {
        List<VolunteerOrganizationEntity> entities = volunteerOrganizationService.findAll();

        return entities.stream()
                .collect(Collectors.groupingBy(entity -> entity.getRegistrationDate().with(DayOfWeek.MONDAY)))
                .entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .sorted(Comparator.comparing(VolunteerOrganizationEntity::getRegistrationDate).reversed())
                        .limit(1))
                .toList();
    }
}
