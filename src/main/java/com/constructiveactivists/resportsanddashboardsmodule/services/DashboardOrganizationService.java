package com.constructiveactivists.resportsanddashboardsmodule.services;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymanagementmodule.services.MissionService;
import com.constructiveactivists.organizationmanagementmodule.entities.OrganizationEntity;
import com.constructiveactivists.organizationmanagementmodule.services.OrganizationService;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermanagementmodule.services.VolunteerOrganizationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
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
        Optional<OrganizationEntity> organization=organizationService.getOrganizationById(organizationId);
        if (!organization.isPresent()) {
            throw new EntityNotFoundException(ORGANIZATION_MESSAGE_ID + organizationId +NOT_FOUND_MESSAGE);
        }
        List<VolunteerOrganizationEntity> entities = volunteerOrganizationService.findAll();
        return entities.stream()
                .filter(entity -> entity.getOrganizationId().equals(organizationId))
                .collect(Collectors.averagingDouble(entity -> entity.getHoursDone() - entity.getHoursCertified()));
    }

    public Double getAverageMonthlyHoursByOrganization(Integer organizationId) {
        Optional<OrganizationEntity> organization=organizationService.getOrganizationById(organizationId);
        if (!organization.isPresent()) {
            throw new EntityNotFoundException(ORGANIZATION_MESSAGE_ID + organizationId +NOT_FOUND_MESSAGE);
        }
        List<VolunteerOrganizationEntity> entities = volunteerOrganizationService.findAll();
        return entities.stream()
                .filter(entity -> entity.getOrganizationId().equals(organizationId))
                .filter(entity -> entity.getMonthlyHours() != null && entity.getMonthlyHours() > 0)
                .collect(Collectors.averagingDouble(VolunteerOrganizationEntity::getMonthlyHours));
    }

    public List<VolunteerOrganizationEntity> getVolunteersByOrganizationAndMonth(Integer organizationId, Integer month, Integer year) {
        // Verificar que la organización existe
        Optional<OrganizationEntity> organization = organizationService.getOrganizationById(organizationId);
        if (!organization.isPresent()) {
            throw new EntityNotFoundException("Organization with ID " + organizationId + " not found.");
        }
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List<VolunteerOrganizationEntity> entities = volunteerOrganizationService.findAll();
        return entities.stream()
                .filter(entity -> entity.getOrganizationId().equals(organizationId))
                .filter(volunteer -> !volunteer.getRegistrationDate().isBefore(startDate) && !volunteer.getRegistrationDate().isAfter(endDate))
                .toList();
    }
}
