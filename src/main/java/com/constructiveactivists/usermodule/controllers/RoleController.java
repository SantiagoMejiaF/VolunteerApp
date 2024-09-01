package com.constructiveactivists.usermodule.controllers;

import com.constructiveactivists.usermodule.entities.RoleEntity;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.usermodule.services.RoleService;
import com.constructiveactivists.usermodule.controllers.configuration.RoleAPI;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.role}")
public class RoleController implements RoleAPI {

    private final RoleService roleService;

    @Override
    public List<RoleEntity> getAllRoles() {
        return roleService.getAllRoles();
    }

    @Override
    public ResponseEntity<RoleEntity> getRoleById(Integer id) {
        Optional<RoleEntity> role = roleService.getRoleById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<RoleEntity> getRoleByType(RoleType roleType) {
        Optional<RoleEntity> role = roleService.getRoleByType(roleType);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<RoleEntity> createRole(@Valid RoleEntity roleEntity) {
        RoleEntity createdRoleEntity = roleService.saveRole(roleEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoleEntity);
    }

    @Override
    public ResponseEntity<RoleEntity> updateRoleByType(RoleType roleType, @Valid RoleEntity updatedRoleEntity) {
        RoleEntity updatedRole = roleService.updateRoleByType(roleType, updatedRoleEntity);
        return ResponseEntity.ok(updatedRole);
    }
}