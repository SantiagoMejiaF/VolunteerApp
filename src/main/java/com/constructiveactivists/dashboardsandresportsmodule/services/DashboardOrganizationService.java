package com.constructiveactivists.dashboardsandresportsmodule.services;

import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymodule.services.mission.MissionService;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.DataShareVolunteerOrganizationService;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.VolunteerOrganizationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.NOT_FOUND_MESSAGE;
import static com.constructiveactivists.configurationmodule.constants.AppConstants.ORGANIZATION_MESSAGE_ID;

@Service
@AllArgsConstructor
public class DashboardOrganizationService {

    private final OrganizationService organizationService;
    private final UserService userService;
    private final MissionService missionService;
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
}
