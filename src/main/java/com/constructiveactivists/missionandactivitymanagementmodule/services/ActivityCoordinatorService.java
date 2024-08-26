package com.constructiveactivists.missionandactivitymanagementmodule.services;

import com.constructiveactivists.authenticationmodule.controllers.configuration.constants.AppConstants;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.repositories.ActivityCoordinatorRepository;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermanagementmodule.entities.enums.RoleType;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ActivityCoordinatorService {

    private final ActivityCoordinatorRepository activityCoordinatorRepository;
    private final UserService userService;

    public ActivityCoordinatorEntity save(ActivityCoordinatorEntity activityCoordinator) {

        UserEntity user = userService.getUserById(activityCoordinator.getUserId().intValue())
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + activityCoordinator.getUserId()
                        + AppConstants.NOT_FOUND_MESSAGE));

        user.getRole().setRoleType(RoleType.COORDINADOR_ACTIVIDAD);
        user.setAuthorizationType(AuthorizationStatus.AUTORIZADO);
        userService.saveUser(user);

        return activityCoordinatorRepository.save(activityCoordinator);
    }

    public List<ActivityCoordinatorEntity> getAll() {
        return activityCoordinatorRepository.findAll();
    }

    public void deleteById(Integer id) {
        activityCoordinatorRepository.deleteById(id);
    }
}
