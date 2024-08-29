package com.constructiveactivists.organizationmanagementmodule.services;

import com.constructiveactivists.authenticationmodule.controllers.configuration.exceptions.BusinessException;
import com.constructiveactivists.organizationmanagementmodule.entities.OrganizationEntity;
import com.constructiveactivists.organizationmanagementmodule.entities.enums.OrganizationTypeEnum;
import com.constructiveactivists.organizationmanagementmodule.entities.enums.SectorTypeEnum;
import com.constructiveactivists.organizationmanagementmodule.entities.enums.VolunteeringTypeEnum;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.organizationmanagementmodule.repositories.OrganizationRepository;
import com.constructiveactivists.usermanagementmodule.entities.enums.RoleType;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerEntity;
import com.constructiveactivists.volunteermanagementmodule.repositories.VolunteerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.constructiveactivists.authenticationmodule.controllers.configuration.constants.AppConstants.NOT_FOUND_MESSAGE;

@Service
@AllArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final VolunteerRepository volunteerRepository;
    private final UserService userService;

    public List<OrganizationEntity> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Optional<OrganizationEntity> getOrganizationById(Integer id) {
        return organizationRepository.findById(id);
    }

    public Optional<OrganizationEntity> getOrganizationByUserId(Integer userId) {
        return organizationRepository.findByUserId(userId);
    }

    public List<SectorTypeEnum> getAllSectors() {
        return Arrays.asList(SectorTypeEnum.values());
    }

    public List<OrganizationTypeEnum> getAllOrganizationTypes() {
        return Arrays.asList(OrganizationTypeEnum.values());
    }

    public List<VolunteeringTypeEnum> getAllVolunteeringTypes() {
        return Arrays.asList(VolunteeringTypeEnum.values());
    }

    public OrganizationEntity saveOrganization(OrganizationEntity organizationEntity) {

        Optional<UserEntity> user = userService.getUserById(organizationEntity.getUserId());

        if (user.isEmpty()) {
            throw new EntityNotFoundException("El usuario con ID " + organizationEntity.getUserId() +
                    NOT_FOUND_MESSAGE);
        }

        userService.updateUserRoleType(organizationEntity.getUserId(), RoleType.ORGANIZACION);

        return organizationRepository.save(organizationEntity);
    }

    public OrganizationEntity updateOrganization(Integer id, OrganizationEntity updateRequest) {
        OrganizationEntity existingOrganization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organización no encontrada con id " + id));
        existingOrganization.setResponsiblePersonPhoneNumber(updateRequest.getResponsiblePersonPhoneNumber());
        existingOrganization.setOrganizationName(updateRequest.getOrganizationName());
        existingOrganization.setOrganizationTypeEnum(updateRequest.getOrganizationTypeEnum());
        existingOrganization.setSectorTypeEnum(updateRequest.getSectorTypeEnum());
        existingOrganization.setVolunteeringTypeEnum(updateRequest.getVolunteeringTypeEnum());
        existingOrganization.setAddress(updateRequest.getAddress());
        return organizationRepository.save(existingOrganization);
    }

    public void approveVolunteer(Integer volunteerId, Integer organizationId) {

        VolunteerEntity volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new EntityNotFoundException("El voluntario con ID " + volunteerId + NOT_FOUND_MESSAGE));

        if (volunteer.getOrganizationId() != null) {
            throw new BusinessException("El voluntario ya está asociado con una organización.");
        }

        volunteer.setOrganizationId(organizationId);
        volunteerRepository.save(volunteer);
    }

    public void deleteOrganization(Integer id) {
        OrganizationEntity organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La organización con ID " + id + NOT_FOUND_MESSAGE));

        userService.deleteUser(organization.getUserId());

        organizationRepository.delete(organization);
    }
}
