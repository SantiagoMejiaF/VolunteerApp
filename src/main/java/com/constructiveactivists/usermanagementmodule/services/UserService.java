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

    private static final String USER_NOT_FOUND_MESSAGE = "El usuario con ID %d no existe en la base de datos.";
    private static final String ROLE_NOT_FOUND_MESSAGE = "El rol con ID %d no existe.";

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(Integer id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, id));
        }
        return user;
    }

    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    public void updateUserRoleType(Integer userId, RoleType roleType) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));

        RoleEntity role = roleService.getRoleById(user.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ROLE_NOT_FOUND_MESSAGE, user.getRoleId())));

        role.setRoleType(roleType);
        roleService.saveRole(role);
    }

    public void updateAuthorizationStatus(Integer userId, AuthorizationStatus authorizationStatus) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));

        user.setAuthorizationType(authorizationStatus);
        userRepository.save(user);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public long getTotalUserCount() {
        return userRepository.count();
    }

    public void deleteUser(Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));

        roleService.deleteRole(user.getRoleId());
        userRepository.delete(user);
    }

    public List<UserEntity> findUsersByAuthorizationStatus(AuthorizationStatus authorizationStatus) {
        return userRepository.findByAuthorizationStatus(authorizationStatus);
    }

    public List<UserEntity> findPendingVolunteersAndOrganizations() {
        List<UserEntity> pendingUsers = userRepository.findByAuthorizationStatus(AuthorizationStatus.PENDIENTE);
        return pendingUsers.stream()
                .filter(user -> {
                    RoleEntity role = roleService.getRoleById(user.getRoleId())
                            .orElse(null);
                    return role != null && (role.getRoleType() == RoleType.VOLUNTARIO || role.getRoleType() == RoleType.ORGANIZACION);
                })
                .toList();
    }


}
