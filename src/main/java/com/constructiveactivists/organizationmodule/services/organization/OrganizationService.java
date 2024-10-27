package com.constructiveactivists.organizationmodule.services.organization;


import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.entities.organization.enums.OrganizationTypeEnum;
import com.constructiveactivists.organizationmodule.entities.organization.enums.SectorTypeEnum;
import com.constructiveactivists.organizationmodule.entities.organization.enums.VolunteeringTypeEnum;
import com.constructiveactivists.organizationmodule.repositories.OrganizationRepository;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.NOT_FOUND_MESSAGE;
import static com.constructiveactivists.configurationmodule.constants.AppConstants.ORGANIZATION_MESSAGE_ID;

@Service
@AllArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserService userService;
    private final PostulationService postulationService;

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
        if(!user.get().getRole().getRoleType().equals(RoleType.SIN_ASIGNAR)){
            throw new BusinessException("El usuario con ID " + organizationEntity.getUserId() +
                    " ya tiene un rol asignado");
        }
        userService.updateUserRoleType(organizationEntity.getUserId(), RoleType.ORGANIZACION);
        organizationEntity.setRegistrationDate(LocalDateTime.now());
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
        existingOrganization.setRequiredCertificationHours(updateRequest.getRequiredCertificationHours());
        existingOrganization.setDescription(updateRequest.getDescription());
        return organizationRepository.save(existingOrganization);
    }

    public void deleteOrganization(Integer id) {
        OrganizationEntity organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException( ORGANIZATION_MESSAGE_ID + id + NOT_FOUND_MESSAGE));
        userService.deleteUser(organization.getUserId());
        organizationRepository.delete(organization);
    }

    public void approveVolunteer(Integer volunteerOrganizationId) {
        postulationService.updateStatusAccept(volunteerOrganizationId);
    }

    public void rejectVolunteer(Integer volunteerOrganizationId) {
        postulationService.updateStatusRefuse(volunteerOrganizationId);
    }

    public List<OrganizationEntity> getTenRecentOrganizations() {
        return organizationRepository.findTop10ByOrderByRegistrationDateDesc();
    }

    public List<OrganizationEntity> getOrganizationsByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return organizationRepository.findByRegistrationYear(startDateTime, endDateTime);
    }

}
