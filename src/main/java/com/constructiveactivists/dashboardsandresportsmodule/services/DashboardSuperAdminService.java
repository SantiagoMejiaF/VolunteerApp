package com.constructiveactivists.dashboardsandresportsmodule.services;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.DataShareVolunteerOrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DashboardSuperAdminService {

    private final ActivityService activityService;
    private final DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;

    public Map<String, Long> getActivityLocalitiesWithFrequency() {
        List<ActivityEntity> activities = activityService.getAll();
        return activities.stream()
                .collect(Collectors.groupingBy(ActivityEntity::getLocality, Collectors.counting()));
    }

    public double getAverageHours() {
        List<DataShareVolunteerOrganizationEntity> dataEntities = dataShareVolunteerOrganizationService.findAll();
        return dataEntities.stream()
                .mapToDouble(entity -> entity.getHoursDone() - entity.getHoursCertified())
                .average()
                .orElse(0.0);
    }

    public Double getAverageMonthlyHours() {
        List<DataShareVolunteerOrganizationEntity> dataEntities = dataShareVolunteerOrganizationService.findAll();
        return dataEntities.stream()
                .filter(entity -> entity.getMonthlyHours() != null && entity.getMonthlyHours() > 0)
                .collect(Collectors.averagingDouble(DataShareVolunteerOrganizationEntity::getMonthlyHours));
    }
}
