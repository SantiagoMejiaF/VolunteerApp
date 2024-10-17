package com.constructiveactivists.usermodule.services;

import com.constructiveactivists.usermodule.entities.RoleEntity;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.usermodule.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;
    private RoleEntity roleEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        roleEntity = new RoleEntity();
        roleEntity.setId(1);
        roleEntity.setRoleType(RoleType.VOLUNTARIO);

        userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("john.doe@example.com");
        userEntity.setAuthorizationType(AuthorizationStatus.PENDIENTE);
        userEntity.setRole(roleEntity);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));

        List<UserEntity> users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("John", users.get(0).getFirstName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> result = userService.getUserById(1);
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1));
        assertEquals("El usuario con ID 1 no existe en la base de datos.", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity result = userService.saveUser(userEntity);
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void testUpdateUserRoleType_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
        when(roleService.getRoleById(1)).thenReturn(Optional.of(roleEntity));

        userService.updateUserRoleType(1, RoleType.ORGANIZACION);

        assertEquals(RoleType.ORGANIZACION, roleEntity.getRoleType());
        verify(roleService, times(1)).saveRole(roleEntity);
    }

    @Test
    void testUpdateUserRoleType_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.updateUserRoleType(1, RoleType.ORGANIZACION));
        assertEquals("El usuario con ID 1 no existe en la base de datos.", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateUserRoleType_RoleNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
        when(roleService.getRoleById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.updateUserRoleType(1, RoleType.ORGANIZACION));
        assertEquals("El rol con ID 1 no existe.", exception.getMessage());
        verify(roleService, times(1)).getRoleById(1);
    }

    @Test
    void testUpdateAuthorizationStatus_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));

        userService.updateAuthorizationStatus(1, AuthorizationStatus.AUTORIZADO);

        assertEquals(AuthorizationStatus.AUTORIZADO, userEntity.getAuthorizationType());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void testUpdateAuthorizationStatus_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.updateAuthorizationStatus(1, AuthorizationStatus.AUTORIZADO));
        assertEquals("El usuario con ID 1 no existe en la base de datos.", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
        doNothing().when(userRepository).delete(userEntity);

        userService.deleteUser(1);

        verify(userRepository, times(1)).delete(userEntity);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(1));
        assertEquals("El usuario con ID 1 no existe en la base de datos.", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testFindUsersByAuthorizationStatus() {
        when(userRepository.findByAuthorizationStatus(AuthorizationStatus.PENDIENTE)).thenReturn(List.of(userEntity));

        List<UserEntity> users = userService.findUsersByAuthorizationStatus(AuthorizationStatus.PENDIENTE);
        assertEquals(1, users.size());
        assertEquals(AuthorizationStatus.PENDIENTE, users.get(0).getAuthorizationType());
        verify(userRepository, times(1)).findByAuthorizationStatus(AuthorizationStatus.PENDIENTE);
    }

    @Test
    void testGetTotalUserCount() {
        when(userRepository.count()).thenReturn(100L);

        long count = userService.getTotalUserCount();
        assertEquals(100, count);
        verify(userRepository, times(1)).count();
    }

    @Test
    void testUpdateUser_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));

        UserEntity updatedUser = new UserEntity();
        updatedUser.setFirstName("Jane");
        updatedUser.setLastName("Doe");
        updatedUser.setImage("new-image.png");

        userService.updateUser(1, updatedUser);

        verify(userRepository, times(1)).save(userEntity);
        assertEquals("Jane", userEntity.getFirstName());
        assertEquals("Doe", userEntity.getLastName());
        assertEquals("new-image.png", userEntity.getImage());
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.updateUser(1, userEntity);
        });

        assertEquals("El usuario con ID 1 no existe en la base de datos.", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testFindByEmail_Success() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> result = userService.findByEmail("john.doe@example.com");

        assertTrue(result.isPresent());
        assertEquals("john.doe@example.com", result.get().getEmail());
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void testFindByEmail_NotFound() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        Optional<UserEntity> result = userService.findByEmail("john.doe@example.com");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void testFindPendingVolunteersAndOrganizations_Success() {
        when(userRepository.findByAuthorizationStatus(AuthorizationStatus.PENDIENTE)).thenReturn(List.of(userEntity));

        when(roleService.getRoleById(1)).thenReturn(Optional.of(roleEntity));

        List<UserEntity> result = userService.findPendingVolunteersAndOrganizations();

        assertEquals(1, result.size());
        assertEquals(RoleType.VOLUNTARIO, result.get(0).getRole().getRoleType());
        verify(userRepository, times(1)).findByAuthorizationStatus(AuthorizationStatus.PENDIENTE);
        verify(roleService, times(1)).getRoleById(1);
    }

    @Test
    void testFindPendingVolunteersAndOrganizations_NoResults() {
        when(userRepository.findByAuthorizationStatus(AuthorizationStatus.PENDIENTE)).thenReturn(List.of());

        List<UserEntity> result = userService.findPendingVolunteersAndOrganizations();

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByAuthorizationStatus(AuthorizationStatus.PENDIENTE);
    }

    @Test
    void testFindPendingVolunteersAndOrganizations_RoleIsNull() {
        when(roleService.getRoleById(1)).thenReturn(Optional.empty());

        when(userRepository.findByAuthorizationStatus(AuthorizationStatus.PENDIENTE)).thenReturn(List.of(userEntity));

        List<UserEntity> result = userService.findPendingVolunteersAndOrganizations();

        assertTrue(result.isEmpty());
        verify(roleService, times(1)).getRoleById(1);
    }

    @Test
    void testFindPendingVolunteersAndOrganizations_RoleIsVolunteer() {
        roleEntity.setRoleType(RoleType.VOLUNTARIO);
        when(roleService.getRoleById(1)).thenReturn(Optional.of(roleEntity));

        when(userRepository.findByAuthorizationStatus(AuthorizationStatus.PENDIENTE)).thenReturn(List.of(userEntity));

        List<UserEntity> result = userService.findPendingVolunteersAndOrganizations();

        assertEquals(1, result.size());
        verify(roleService, times(1)).getRoleById(1);
    }

    @Test
    void testFindPendingVolunteersAndOrganizations_RoleIsOrganization() {
        roleEntity.setRoleType(RoleType.ORGANIZACION);
        when(roleService.getRoleById(1)).thenReturn(Optional.of(roleEntity));

        when(userRepository.findByAuthorizationStatus(AuthorizationStatus.PENDIENTE)).thenReturn(List.of(userEntity));

        List<UserEntity> result = userService.findPendingVolunteersAndOrganizations();

        assertEquals(1, result.size());
        verify(roleService, times(1)).getRoleById(1);
    }

    @Test
    void testFindPendingVolunteersAndOrganizations_RoleIsNeitherVolunteerNorOrganization() {
        roleEntity.setRoleType(RoleType.SUPER_ADMIN);
        when(roleService.getRoleById(1)).thenReturn(Optional.of(roleEntity));

        when(userRepository.findByAuthorizationStatus(AuthorizationStatus.PENDIENTE)).thenReturn(List.of(userEntity));

        List<UserEntity> result = userService.findPendingVolunteersAndOrganizations();

        assertTrue(result.isEmpty());
        verify(roleService, times(1)).getRoleById(1);
    }
}
