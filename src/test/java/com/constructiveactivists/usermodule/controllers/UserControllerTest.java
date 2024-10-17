package com.constructiveactivists.usermodule.controllers;

import com.constructiveactivists.usermodule.controllers.request.UserRequest;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.usermodule.mapper.UserUpdateMapper;
import com.constructiveactivists.usermodule.services.ApprovalService;
import com.constructiveactivists.usermodule.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ApprovalService approvalService;

    @Mock
    private UserUpdateMapper userUpdateMapper;

    @InjectMocks
    private UserController userController;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("john.doe@example.com");
        userEntity.setAuthorizationType(AuthorizationStatus.PENDIENTE);
    }

    @Test
    void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(List.of(userEntity));

        List<UserEntity> result = userController.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById_Found() {
        when(userService.getUserById(1)).thenReturn(Optional.of(userEntity));

        ResponseEntity<UserEntity> response = userController.getUserById(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userEntity, response.getBody());
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userService.getUserById(1)).thenReturn(Optional.empty());

        ResponseEntity<UserEntity> response = userController.getUserById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void testUpdateUserRoleType() {
        doNothing().when(userService).updateUserRoleType(1, RoleType.COORDINADOR_ACTIVIDAD);

        ResponseEntity<Void> response = userController.updateUserRoleType(1, RoleType.COORDINADOR_ACTIVIDAD);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).updateUserRoleType(1, RoleType.COORDINADOR_ACTIVIDAD);
    }

    @Test
    void testUpdateAuthorizationStatus() {
        doNothing().when(userService).updateAuthorizationStatus(1, AuthorizationStatus.AUTORIZADO);

        ResponseEntity<Void> response = userController.updateAuthorizationStatus(1, AuthorizationStatus.AUTORIZADO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).updateAuthorizationStatus(1, AuthorizationStatus.AUTORIZADO);
    }

    @Test
    void testUpdateUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("Jane");

        when(userUpdateMapper.toDomain(any(UserRequest.class))).thenReturn(userEntity);

        ResponseEntity<Void> response = userController.updateUser(1, userRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).updateUser(any(Integer.class), any(UserEntity.class));
    }

    @Test
    void testSendApprovalOrRejectionEmail_Approved() {
        doNothing().when(approvalService).sendApprovalResponse(1, true);

        ResponseEntity<String> response = userController.sendApprovalOrRejectionEmail(1, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario aprobado correctamente.", response.getBody());
        verify(approvalService, times(1)).sendApprovalResponse(1, true);
    }

    @Test
    void testSendApprovalOrRejectionEmail_Rejected() {
        doNothing().when(approvalService).sendApprovalResponse(1, false);

        ResponseEntity<String> response = userController.sendApprovalOrRejectionEmail(1, false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario rechazado correctamente.", response.getBody());
        verify(approvalService, times(1)).sendApprovalResponse(1, false);
    }

    @Test
    void testGetTotalUserCount() {
        when(userService.getTotalUserCount()).thenReturn(10L);

        ResponseEntity<Long> response = userController.getTotalUserCount();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10L, response.getBody());
        verify(userService, times(1)).getTotalUserCount();
    }

    @Test
    void testGetUsersByAuthorizationStatus() {
        when(userService.findUsersByAuthorizationStatus(AuthorizationStatus.PENDIENTE)).thenReturn(List.of(userEntity));

        ResponseEntity<List<UserEntity>> response = userController.getUsersByAuthorizationStatus(AuthorizationStatus.PENDIENTE);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).findUsersByAuthorizationStatus(AuthorizationStatus.PENDIENTE);
    }

    @Test
    void testGetPendingVolunteersAndOrganizations() {
        when(userService.findPendingVolunteersAndOrganizations()).thenReturn(List.of(userEntity));

        ResponseEntity<List<UserEntity>> response = userController.getPendingVolunteersAndOrganizations();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).findPendingVolunteersAndOrganizations();
    }
}
