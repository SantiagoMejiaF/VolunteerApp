package com.constructiveactivists.configurationmodule.security.service;

import com.constructiveactivists.configurationmodule.security.model.CustomUserDetails;
import com.constructiveactivists.usermodule.entities.RoleEntity;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.usermodule.services.RoleService;
import com.constructiveactivists.usermodule.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private UserEntity mockUser;
    private RoleEntity mockRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new UserEntity();
        mockUser.setId(1);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("john.doe@example.com");
        mockUser.setAuthorizationType(AuthorizationStatus.AUTORIZADO);
        mockUser.setRegistrationDate(java.time.LocalDate.now());

        mockRole = new RoleEntity();
        mockRole.setId(1);
        mockRole.setRoleType(RoleType.SUPER_ADMIN);
        mockUser.setRole(mockRole);
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {

        when(userService.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(roleService.getRoleById(anyInt())).thenReturn(Optional.of(mockRole));

        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername("john.doe@example.com");

        assertNotNull(userDetails);
        assertEquals("john.doe@example.com", userDetails.getUsername());
        assertEquals(mockRole, userDetails.getRoles().get(0));

        verify(userService, times(1)).findByEmail("john.doe@example.com");
        verify(roleService, times(1)).getRoleById(mockUser.getRole().getId());
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {

        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("nonexistent@example.com"));

        verify(userService, times(1)).findByEmail("nonexistent@example.com");
        verify(roleService, never()).getRoleById(anyInt());
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenRoleNotFound() {

        when(userService.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(roleService.getRoleById(anyInt())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("john.doe@example.com"));

        verify(userService, times(1)).findByEmail("john.doe@example.com");
        verify(roleService, times(1)).getRoleById(mockUser.getRole().getId());
    }
}
