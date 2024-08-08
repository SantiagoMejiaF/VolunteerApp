package com.constructiveactivists.organizationmanagementmodule.services;

import com.constructiveactivists.organizationmanagementmodule.entities.OrganizationEntity;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.organizationmanagementmodule.repositories.OrganizationRepository;
import com.constructiveactivists.usermanagementmodule.entities.enums.RoleType;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserService userService;

    public List<OrganizationEntity> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Optional<OrganizationEntity> getOrganizationById(Integer id) {
        return organizationRepository.findById(id);
    }

    public OrganizationEntity saveOrganization(OrganizationEntity organizationEntity) {

        Optional<UserEntity> user = userService.getUserById(organizationEntity.getUserId());

        if (user.isEmpty()) {
            throw new EntityNotFoundException("El usuario con ID " + organizationEntity.getUserId() +
                    " no existe en la base de datos.");
        }

        userService.updateUserRoleType(organizationEntity.getUserId(), RoleType.ORGANIZACION);

        return organizationRepository.save(organizationEntity);
    }

    public void deleteOrganization(Integer id) {
        OrganizationEntity organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La organización con ID " + id + " no existe en la base de datos."));
        organizationRepository.delete(organization);
    }
}
