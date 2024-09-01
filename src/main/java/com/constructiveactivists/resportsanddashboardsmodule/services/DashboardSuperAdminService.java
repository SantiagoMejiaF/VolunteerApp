package com.constructiveactivists.resportsanddashboardsmodule.services;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.services.ActivityService;
import com.constructiveactivists.postulationmanagementmodule.entities.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.postulationmanagementmodule.entities.PostulationEntity;
import com.constructiveactivists.postulationmanagementmodule.entities.VolunteerOrganizationEntity;
import com.constructiveactivists.postulationmanagementmodule.services.DataShareVolunteerOrganizationService;
import com.constructiveactivists.postulationmanagementmodule.services.PostulationService;
import com.constructiveactivists.postulationmanagementmodule.services.VolunteerOrganizationService;
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
    private final PostulationService postulationService;
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


    public List<PostulationEntity> getRecentVolunteersByWeek() {
        List<PostulationEntity> postulationEntities = postulationService.findAll();

        return postulationEntities.stream()
                .collect(Collectors.groupingBy(entity -> entity.getRegistrationDate().with(DayOfWeek.MONDAY)))
                .entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .sorted(Comparator.comparing(PostulationEntity::getRegistrationDate).reversed())
                        .limit(1))
                .toList();
    }

}
