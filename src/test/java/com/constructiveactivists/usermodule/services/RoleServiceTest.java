package com.constructiveactivists.usermodule.services;

import com.constructiveactivists.usermodule.entities.RoleEntity;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.usermodule.repositories.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private RoleEntity roleEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        roleEntity = new RoleEntity();
        roleEntity.setId(1);
        roleEntity.setRoleType(RoleType.VOLUNTARIO);
    }

    @Test
    void testGetAllRoles() {
        List<RoleEntity> roles = Collections.singletonList(roleEntity);

        when(roleRepository.findAll()).thenReturn(roles);

        List<RoleEntity> result = roleService.getAllRoles();
        assertEquals(1, result.size());
        assertEquals(RoleType.VOLUNTARIO, result.get(0).getRoleType());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testGetRoleById_Success() {
        when(roleRepository.findById(1)).thenReturn(Optional.of(roleEntity));

        Optional<RoleEntity> result = roleService.getRoleById(1);
        assertTrue(result.isPresent());
        assertEquals(RoleType.VOLUNTARIO, result.get().getRoleType());
        verify(roleRepository, times(1)).findById(1);
    }

    @Test
    void testGetRoleById_NotFound() {
        when(roleRepository.findById(1)).thenReturn(Optional.empty());

        Optional<RoleEntity> result = roleService.getRoleById(1);
        assertFalse(result.isPresent());
        verify(roleRepository, times(1)).findById(1);
    }

    @Test
    void testGetRoleByType_Success() {
        when(roleRepository.findByRoleType(RoleType.VOLUNTARIO)).thenReturn(Optional.of(roleEntity));

        Optional<RoleEntity> result = roleService.getRoleByType(RoleType.VOLUNTARIO);
        assertTrue(result.isPresent());
        assertEquals(RoleType.VOLUNTARIO, result.get().getRoleType());
        verify(roleRepository, times(1)).findByRoleType(RoleType.VOLUNTARIO);
    }

    @Test
    void testGetRoleByType_NotFound() {
        when(roleRepository.findByRoleType(RoleType.VOLUNTARIO)).thenReturn(Optional.empty());

        Optional<RoleEntity> result = roleService.getRoleByType(RoleType.VOLUNTARIO);
        assertFalse(result.isPresent());
        verify(roleRepository, times(1)).findByRoleType(RoleType.VOLUNTARIO);
    }

    @Test
    void testCreateRole() {
        when(roleRepository.save(any(RoleEntity.class))).thenReturn(roleEntity);

        RoleEntity result = roleService.createRole(roleEntity);
        assertNotNull(result);
        assertEquals(RoleType.VOLUNTARIO, result.getRoleType());
        verify(roleRepository, times(1)).save(roleEntity);
    }

    @Test
    void testUpdateRoleByType_Success() {
        RoleEntity updatedRole = new RoleEntity();
        updatedRole.setRoleType(RoleType.SUPER_ADMIN);

        when(roleRepository.findByRoleType(RoleType.VOLUNTARIO)).thenReturn(Optional.of(roleEntity));
        when(roleRepository.save(any(RoleEntity.class))).thenReturn(updatedRole);

        RoleEntity result = roleService.updateRoleByType(RoleType.VOLUNTARIO, updatedRole);
        assertNotNull(result);
        assertEquals(RoleType.SUPER_ADMIN, result.getRoleType());
        verify(roleRepository, times(1)).findByRoleType(RoleType.VOLUNTARIO);
        verify(roleRepository, times(1)).save(roleEntity);
    }

    @Test
    void testUpdateRoleByType_NotFound() {
        RoleEntity updatedRole = new RoleEntity();
        updatedRole.setRoleType(RoleType.SUPER_ADMIN);

        when(roleRepository.findByRoleType(RoleType.VOLUNTARIO)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            roleService.updateRoleByType(RoleType.VOLUNTARIO, updatedRole);
        });

        assertEquals("El tipo de rol VOLUNTARIO no se encontró en la base de datos.", exception.getMessage());
        verify(roleRepository, times(1)).findByRoleType(RoleType.VOLUNTARIO);
    }

    @Test
    void testDeleteRole() {
        doNothing().when(roleRepository).deleteById(1);

        roleService.deleteRole(1);
        verify(roleRepository, times(1)).deleteById(1);
    }

    @Test
    void testSaveRole() {
        when(roleRepository.save(any(RoleEntity.class))).thenReturn(roleEntity);

        RoleEntity result = roleService.saveRole(roleEntity);

        assertNotNull(result);
        assertEquals(RoleType.VOLUNTARIO, result.getRoleType());

        verify(roleRepository, times(1)).save(roleEntity);
    }
}
