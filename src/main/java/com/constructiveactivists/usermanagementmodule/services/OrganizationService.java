package com.constructiveactivists.usermanagementmodule.services;

import com.constructiveactivists.usermanagementmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.usermanagementmodule.entities.user.UserEntity;
import com.constructiveactivists.usermanagementmodule.repositories.OrganizationRepository;
import com.constructiveactivists.usermanagementmodule.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    private final UserRepository userRepository;

    public List<OrganizationEntity> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Optional<OrganizationEntity> getOrganizationById(Integer id) {
        return organizationRepository.findById(id);
    }

    public OrganizationEntity saveOrganization(OrganizationEntity organizationEntity) {
        Optional<UserEntity> user = userRepository.findById(organizationEntity.getUserId());

        if (user.isEmpty()) {
            throw new EntityNotFoundException("Usuario con ID " + organizationEntity.getUserId() + " no existe.");
        }
        userRepository.updateRole(organizationEntity.getUserId(), 3);
        return organizationRepository.save(organizationEntity);
    }

    public void deleteOrganization(Integer id) {
        OrganizationEntity organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id " + id));
        organizationRepository.delete(organization);
    }
}
