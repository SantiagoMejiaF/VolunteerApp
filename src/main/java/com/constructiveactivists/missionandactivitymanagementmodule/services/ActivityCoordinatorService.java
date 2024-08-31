package com.constructiveactivists.missionandactivitymanagementmodule.services;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.repositories.ActivityCoordinatorRepository;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermanagementmodule.entities.enums.RoleType;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ActivityCoordinatorService {

    private final ActivityCoordinatorRepository activityCoordinatorRepository;
    private final UserService userService;

    public ActivityCoordinatorEntity save(ActivityCoordinatorEntity activityCoordinator) {
        UserEntity user = new UserEntity();
        user.setFirstName(activityCoordinator.getNameActivityCoordinator());
        user.setLastName(activityCoordinator.getLastNameActivityCoordinator());
        user.setEmail(activityCoordinator.getEmailActivityCoordinator());
        user.getRole().setRoleType(RoleType.COORDINADOR_ACTIVIDAD);
        user.setAuthorizationType(AuthorizationStatus.AUTORIZADO);
        Optional<UserEntity> existingUser = userService.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new EntityExistsException("El correo electrónico " + user.getEmail() + " ya está registrado.");
        }
        userService.saveUser(user);
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
}
