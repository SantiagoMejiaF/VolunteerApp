package com.constructiveactivists.usermodule.controllers;

import com.constructiveactivists.usermodule.entities.RoleEntity;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.usermodule.services.RoleService;
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

class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private RoleEntity roleEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roleEntity = new RoleEntity();
        roleEntity.setId(1);
        roleEntity.setRoleType(RoleType.COORDINADOR_ACTIVIDAD);
    }

    @Test
    void testGetAllRoles() {
        List<RoleEntity> roles = List.of(roleEntity);
        when(roleService.getAllRoles()).thenReturn(roles);

        List<RoleEntity> result = roleController.getAllRoles();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(roleService, times(1)).getAllRoles();
    }

    @Test
    void testGetRoleById_Found() {
        when(roleService.getRoleById(1)).thenReturn(Optional.of(roleEntity));

        ResponseEntity<RoleEntity> response = roleController.getRoleById(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roleEntity, response.getBody());
        verify(roleService, times(1)).getRoleById(1);
    }

    @Test
    void testGetRoleById_NotFound() {
        when(roleService.getRoleById(1)).thenReturn(Optional.empty());

        ResponseEntity<RoleEntity> response = roleController.getRoleById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(roleService, times(1)).getRoleById(1);
    }

    @Test
    void testGetRoleByType_Found() {
        when(roleService.getRoleByType(RoleType.COORDINADOR_ACTIVIDAD)).thenReturn(Optional.of(roleEntity));

        ResponseEntity<RoleEntity> response = roleController.getRoleByType(RoleType.COORDINADOR_ACTIVIDAD);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roleEntity, response.getBody());
        verify(roleService, times(1)).getRoleByType(RoleType.COORDINADOR_ACTIVIDAD);
    }

    @Test
    void testGetRoleByType_NotFound() {
        when(roleService.getRoleByType(RoleType.COORDINADOR_ACTIVIDAD)).thenReturn(Optional.empty());

        ResponseEntity<RoleEntity> response = roleController.getRoleByType(RoleType.COORDINADOR_ACTIVIDAD);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(roleService, times(1)).getRoleByType(RoleType.COORDINADOR_ACTIVIDAD);
    }

    @Test
    void testCreateRole() {
        when(roleService.saveRole(any(RoleEntity.class))).thenReturn(roleEntity);

        ResponseEntity<RoleEntity> response = roleController.createRole(roleEntity);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(roleEntity, response.getBody());
        verify(roleService, times(1)).saveRole(any(RoleEntity.class));
    }

    @Test
    void testUpdateRoleByType() {
        when(roleService.updateRoleByType(eq(RoleType.COORDINADOR_ACTIVIDAD), any(RoleEntity.class)))
                .thenReturn(roleEntity);

        ResponseEntity<RoleEntity> response = roleController.updateRoleByType(RoleType.COORDINADOR_ACTIVIDAD, roleEntity);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roleEntity, response.getBody());
        verify(roleService, times(1)).updateRoleByType(eq(RoleType.COORDINADOR_ACTIVIDAD), any(RoleEntity.class));
    }
}
