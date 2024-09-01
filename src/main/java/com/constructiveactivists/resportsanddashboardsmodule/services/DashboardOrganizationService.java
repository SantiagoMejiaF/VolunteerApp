package com.constructiveactivists.resportsanddashboardsmodule.services;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymanagementmodule.services.MissionService;
import com.constructiveactivists.organizationmanagementmodule.entities.OrganizationEntity;
import com.constructiveactivists.organizationmanagementmodule.services.OrganizationService;
import com.constructiveactivists.postulationmanagementmodule.entities.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.postulationmanagementmodule.entities.PostulationEntity;
import com.constructiveactivists.postulationmanagementmodule.services.DataShareVolunteerOrganizationService;
import com.constructiveactivists.postulationmanagementmodule.services.PostulationService;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import com.constructiveactivists.postulationmanagementmodule.entities.VolunteerOrganizationEntity;
import com.constructiveactivists.postulationmanagementmodule.services.VolunteerOrganizationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.constructiveactivists.authenticationmodule.controllers.configuration.constants.AppConstants.NOT_FOUND_MESSAGE;
import static com.constructiveactivists.authenticationmodule.controllers.configuration.constants.AppConstants.ORGANIZATION_MESSAGE_ID;

@Service
@AllArgsConstructor
public class DashboardOrganizationService {

    private final OrganizationService organizationService;
    private final UserService userService;
    private final MissionService missionService;
    private final VolunteerOrganizationService volunteerOrganizationService;
    private final PostulationService postulationService;
    private final DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;

    private Optional<OrganizationEntity> getOrganizationById(Integer organizationId) {
        Optional<OrganizationEntity> organization = organizationService.getOrganizationById(organizationId);
        if (!organization.isPresent()) {
            throw new EntityNotFoundException(ORGANIZATION_MESSAGE_ID + organizationId + NOT_FOUND_MESSAGE);
        }
        return organization;
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
        return missionService.getAll().stream()
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

}
