package com.constructiveactivists.dashboardsandresportsmodule.services;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.DataShareVolunteerOrganizationService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
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
    private final DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;
    private final PostulationService postulationService;

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

    public List<PostulationEntity> getRecentVolunteersByWeek() {
        List<PostulationEntity> postulationEntities = postulationService.findAll();
        return postulationEntities.stream()
                .filter(entity -> OrganizationStatusEnum.ACEPTADO.equals(entity.getStatus()))
                .collect(Collectors.groupingBy(entity -> entity.getRegistrationDate().with(DayOfWeek.MONDAY)))
                .entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .sorted(Comparator.comparing(PostulationEntity::getRegistrationDate).reversed())
                        .limit(1))
                .toList();
    }
}
