package com.constructiveactivists.organizationmodule.services.activitycoordinator;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.organizationmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import com.constructiveactivists.organizationmodule.entities.activitycoordinator.CoordinatorAvailabilityModel;
import com.constructiveactivists.organizationmodule.repositories.ActivityCoordinatorRepository;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.usermodule.services.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.USER_NOT_FOUND;

@AllArgsConstructor
@Service
public class ActivityCoordinatorService {

    private static final RoleType COORDINATOR_ROLE = RoleType.COORDINADOR_ACTIVIDAD;
    private final ActivityCoordinatorRepository activityCoordinatorRepository;
    private final UserService userService;
    private final ActivityService activityService;
    private final OrganizationService organizationService;

    public ActivityCoordinatorEntity save(ActivityCoordinatorEntity activityCoordinator, Integer userId) {
        UserEntity user = userService.getUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        user.getRole().setRoleType(COORDINATOR_ROLE);
        user.setAuthorizationType(AuthorizationStatus.AUTORIZADO);
        userService.saveUser(user);
        Optional<OrganizationEntity> organizationOpt = organizationService.getOrganizationById(activityCoordinator.getOrganizationId());
        if (organizationOpt.isEmpty()) {
            throw new BusinessException("La organización con ID " + activityCoordinator.getOrganizationId() + " no existe.");
        } else {
            organizationOpt.get();
        }
        activityCoordinator.setUserId(userId);
        return activityCoordinatorRepository.save(activityCoordinator);
    }

    public Optional<ActivityCoordinatorEntity> getById(Integer id){
     return activityCoordinatorRepository.findById(id);
    }

    public List<ActivityCoordinatorEntity> getAll() {
        return activityCoordinatorRepository.findAll();
    }

    public void deleteById(Integer id) {
        activityCoordinatorRepository.deleteById(id);
    }

    public List<ActivityCoordinatorEntity> findAvailableCoordinators(
            CoordinatorAvailabilityModel coordinatorAvailabilityModel) {
        Optional <OrganizationEntity> organization =  organizationService.getOrganizationById(coordinatorAvailabilityModel.getOrganizationId());
        if(organization.isEmpty()){
            throw new EntityExistsException("La organización con id " + coordinatorAvailabilityModel.getOrganizationId() + " no existe.");
        }
        List<ActivityCoordinatorEntity> coordinators = activityCoordinatorRepository
                .findByOrganizationId(coordinatorAvailabilityModel.getOrganizationId());
        return coordinators.stream()
                .filter(coordinator -> isCoordinatorAvailable(coordinator, coordinatorAvailabilityModel.getDate(), coordinatorAvailabilityModel.getStartTime(), coordinatorAvailabilityModel.getEndTime()))
                .toList();
    }

    private boolean isCoordinatorAvailable(ActivityCoordinatorEntity coordinator,
                                           LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<ActivityEntity> activities = activityService.findAllByActivityCoordinator(coordinator.getId());
        return activities.stream()
                .filter(activity -> activity.getDate().isEqual(date))
                .noneMatch(activity -> isTimeOverlap(activity, startTime, endTime));
    }

    boolean isTimeOverlap(ActivityEntity activity, LocalTime startTime, LocalTime endTime) {
        return activity.getStartTime().isBefore(endTime) && activity.getEndTime().isAfter(startTime);
    }

    public List<ActivityCoordinatorEntity> findByOrganizationId(Integer organizationId) {
        return activityCoordinatorRepository.findByOrganizationId(organizationId);
    }

    public Optional<ActivityCoordinatorEntity> getCoordinatorById(Integer id) {
        return activityCoordinatorRepository.findById(id);
    }
}
