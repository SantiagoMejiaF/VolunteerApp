package com.constructiveactivists.usermanagementmodule.services;

import com.constructiveactivists.usermanagementmodule.entities.RoleEntity;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermanagementmodule.entities.enums.RoleType;
import com.constructiveactivists.usermanagementmodule.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public UserEntity saveUser(UserEntity user) {return userRepository.save(user);}

    public void updateUserRoleType(Integer userId, RoleType roleType) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + userId + " no existe."));

        RoleEntity role = roleService.getRoleById(user.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("El rol con ID " + user.getRoleId() + " no existe."));

        role.setRoleType(roleType);
        roleService.saveRole(role);
    }

    public void updateAuthorizationStatus(Integer userId, AuthorizationStatus authorizationStatus) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + userId + " no existe en la base de datos."));

        user.setAuthorizationType(authorizationStatus);
        userRepository.save(user);
    }

    public Optional<UserEntity> findByEmail(String email) {return userRepository.findByEmail(email); }

    public void deleteUser(Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + userId + " no existe."));

        roleService.deleteRole(user.getRoleId());

        userRepository.delete(user);
    }

    public long countOrganizationsByAuthorizationStatus(AuthorizationStatus authorizationStatus) {
        return userRepository.countOrganizationsByStatus(authorizationStatus.name());
    }

    public List<UserEntity> findUsersByAuthorizationStatus(AuthorizationStatus authorizationStatus) {
        return userRepository.findByAuthorizationStatus(authorizationStatus);
    }


}
