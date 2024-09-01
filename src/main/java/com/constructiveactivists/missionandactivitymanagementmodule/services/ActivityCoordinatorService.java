package com.constructiveactivists.missionandactivitymanagementmodule.services;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.activitycoordinator.CoordinatorAvailabilityModel;
import com.constructiveactivists.missionandactivitymanagementmodule.repositories.ActivityCoordinatorRepository;
import com.constructiveactivists.organizationmanagementmodule.entities.OrganizationEntity;
import com.constructiveactivists.organizationmanagementmodule.services.OrganizationService;
import com.constructiveactivists.usermanagementmodule.entities.RoleEntity;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermanagementmodule.entities.enums.RoleType;
import com.constructiveactivists.usermanagementmodule.repositories.RoleRepository;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ActivityCoordinatorService {

    private final ActivityCoordinatorRepository activityCoordinatorRepository;
    private final UserService userService;
    private final ActivityService activityService;
    private final OrganizationService organizationService;
    private final RoleRepository roleRepository;

    public ActivityCoordinatorEntity save(ActivityCoordinatorEntity activityCoordinator, UserEntity user) {
        Optional<UserEntity> existingUserOpt = userService.findByEmail(user.getEmail());
        if (existingUserOpt.isPresent()) {
            throw new EntityExistsException("El correo electrónico " + user.getEmail() + " ya está registrado.");
        }
        Optional<OrganizationEntity> organization = organizationService.getOrganizationById(activityCoordinator.getOrganizationId());
        if (organization.isEmpty()) {
            throw new EntityExistsException("La organización con id " + activityCoordinator.getOrganizationId() + " no existe.");
        }
        UserEntity newUser = configureNewUser(user);
        userService.saveUser(newUser);
        activityCoordinator.setUserId(newUser.getId());
        return activityCoordinatorRepository.save(activityCoordinator);
    }

    private UserEntity configureNewUser(UserEntity user) {
        RoleEntity role = new RoleEntity();
        role.setRoleType(RoleType.COORDINADOR_ACTIVIDAD);
        role = roleRepository.save(role);
        user.setRole(role);
        user.setAuthorizationType(AuthorizationStatus.AUTORIZADO);
        user.setRegistrationDate(LocalDate.now());
        return user;
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

    private boolean isTimeOverlap(ActivityEntity activity, LocalTime startTime, LocalTime endTime) {
        return activity.getStartTime().isBefore(endTime) && activity.getEndTime().isAfter(startTime);
    }
}
