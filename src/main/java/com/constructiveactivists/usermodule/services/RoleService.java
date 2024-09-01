package com.constructiveactivists.usermodule.services;

import com.constructiveactivists.usermodule.entities.RoleEntity;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.usermodule.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<RoleEntity> getRoleById(Integer id) {
        return roleRepository.findById(id);
    }

    public Optional<RoleEntity> getRoleByType(RoleType roleType) {
        return roleRepository.findByRoleType(roleType);
    }

    public RoleEntity createRole(RoleEntity roleEntity) {
        return roleRepository.save(roleEntity);
    }

    public RoleEntity saveRole(RoleEntity roleEntity) {
        return roleRepository.save(roleEntity);
    }

    public RoleEntity updateRoleByType(RoleType roleType, RoleEntity updatedRoleEntity) {
        RoleEntity existingRole = roleRepository.findByRoleType(roleType)
                .orElseThrow(() -> new EntityNotFoundException("El tipo de rol " + roleType + " no se encontró en la " +
                        "base de datos."));

        existingRole.setRoleType(updatedRoleEntity.getRoleType());
        return roleRepository.save(existingRole);
    }

    public void deleteRole(Integer roleId) {
        roleRepository.deleteById(roleId);
    }
}
