package com.constructiveactivists.externalservicesmodule.services;

import com.constructiveactivists.configurationmodule.security.model.JwtUtil;
import com.constructiveactivists.configurationmodule.security.service.CustomUserDetailsService;
import com.constructiveactivists.externalservicesmodule.controllers.response.AuthenticationResponse;
import com.constructiveactivists.externalservicesmodule.models.TokenModel;
import com.constructiveactivists.usermodule.entities.RoleEntity;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.usermodule.services.RoleService;
import com.constructiveactivists.usermodule.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private GoogleService googleService;

    private TokenModel tokenModel;
    private UserEntity userEntity;
    private RoleEntity roleEntity;
    private Map<String, Object> userInfo;

    @BeforeEach
    void setUp() {
        tokenModel = new TokenModel("googleToken123");

        roleEntity = new RoleEntity();
        roleEntity.setId(1);
        roleEntity.setRoleType(RoleType.SIN_ASIGNAR);

        userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setRole(roleEntity);
        userEntity.setAuthorizationType(AuthorizationStatus.PENDIENTE);
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("johndoe@example.com");
        userEntity.setImage("image-url");
        userEntity.setRegistrationDate(LocalDate.now());

        userInfo = new HashMap<>();
        userInfo.put("email", "johndoe@example.com");
        userInfo.put("given_name", "John");
        userInfo.put("family_name", "Doe");
        userInfo.put("picture", "image-url");
    }

    @Test
    void testAuthenticationByGoogle_WhenUserExists() throws IOException {
        when(googleService.fetchUserInfo(tokenModel.getValue())).thenReturn(userInfo);
        when(userService.findByEmail("johndoe@example.com")).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = mock(UserDetails.class);
        when(customUserDetailsService.loadUserByUsername("johndoe@example.com")).thenReturn(userDetails);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        when(jwtUtil.generateToken(authentication)).thenReturn("jwtToken123");

        AuthenticationResponse response = authenticationService.authenticationByGoogle(tokenModel);

        assertNotNull(response);
        assertEquals("jwtToken123", response.getToken());
        assertEquals("johndoe@example.com", response.getUser().getEmail());

        verify(googleService, times(1)).fetchUserInfo(tokenModel.getValue());
        verify(userService, times(1)).findByEmail("johndoe@example.com");
        verify(customUserDetailsService, times(1)).loadUserByUsername("johndoe@example.com");
        verify(jwtUtil, times(1)).generateToken(authentication);
    }

    @Test
    void testAuthenticationByGoogle_WhenUserIsNew() throws IOException {
        when(googleService.fetchUserInfo(tokenModel.getValue())).thenReturn(userInfo);
        when(userService.findByEmail("johndoe@example.com")).thenReturn(Optional.empty());

        when(roleService.createRole(any(RoleEntity.class))).thenReturn(roleEntity);

        when(roleService.getRoleById(roleEntity.getId())).thenReturn(Optional.of(roleEntity));

        when(userService.saveUser(any(UserEntity.class))).thenReturn(userEntity);

        UserDetails userDetails = mock(UserDetails.class);
        when(customUserDetailsService.loadUserByUsername("johndoe@example.com")).thenReturn(userDetails);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        when(jwtUtil.generateToken(authentication)).thenReturn("jwtToken123");

        AuthenticationResponse response = authenticationService.authenticationByGoogle(tokenModel);

        assertNotNull(response);
        assertEquals("jwtToken123", response.getToken());
        assertEquals("johndoe@example.com", response.getUser().getEmail());

        verify(googleService, times(1)).fetchUserInfo(tokenModel.getValue());
        verify(userService, times(1)).findByEmail("johndoe@example.com");
        verify(roleService, times(1)).createRole(any(RoleEntity.class));
        verify(roleService, times(1)).getRoleById(roleEntity.getId());
        verify(userService, times(1)).saveUser(any(UserEntity.class));
        verify(customUserDetailsService, times(1)).loadUserByUsername("johndoe@example.com");
        verify(jwtUtil, times(1)).generateToken(authentication);
    }

    @Test
    void testAuthenticationByGoogle_ThrowsIOException() throws IOException {
        when(googleService.fetchUserInfo(tokenModel.getValue())).thenThrow(new IOException("Google API error"));

        Exception exception = assertThrows(IOException.class, () -> {
            authenticationService.authenticationByGoogle(tokenModel);
        });

        assertTrue(exception.getMessage().contains("Google API error"));

        verify(googleService, times(1)).fetchUserInfo(tokenModel.getValue());
        verify(userService, never()).findByEmail(anyString());
        verify(customUserDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtUtil, never()).generateToken(any(Authentication.class));
    }
}
