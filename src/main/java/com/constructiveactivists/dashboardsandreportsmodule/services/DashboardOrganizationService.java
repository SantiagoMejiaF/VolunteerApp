package com.constructiveactivists.dashboardsandreportsmodule.services;

import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymodule.services.mission.MissionService;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
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

import static com.constructiveactivists.configurationmodule.constants.AppConstants.NOT_FOUND_MESSAGE;
import static com.constructiveactivists.configurationmodule.constants.AppConstants.ORGANIZATION_MESSAGE_ID;

@Service
@AllArgsConstructor
public class DashboardOrganizationService {

    private final OrganizationService organizationService;
    private final UserService userService;
    private final MissionService missionService;
    private final PostulationService postulationService;
    private final VolunteerOrganizationService volunteerOrganizationService;
    private final DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;

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
}