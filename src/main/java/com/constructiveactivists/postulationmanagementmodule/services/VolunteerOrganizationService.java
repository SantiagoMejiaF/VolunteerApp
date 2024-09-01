package com.constructiveactivists.postulationmanagementmodule.services;

import com.constructiveactivists.postulationmanagementmodule.entities.VolunteerOrganizationEntity;
import com.constructiveactivists.postulationmanagementmodule.repositories.VolunteerOrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VolunteerOrganizationService {

    private final PostulationService postulationService;
    private final VolunteerOrganizationRepository volunteerOrganizationRepository;
    private final DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;

    public List<VolunteerOrganizationEntity> getOrganizationsByVolunteerId(Integer volunteerId) {
        return volunteerOrganizationRepository.findByVolunteerId(volunteerId);
    }

    public List<VolunteerOrganizationEntity> getVolunteersByOrganizationId(Integer organizationId) {
        return volunteerOrganizationRepository.findByOrganizationId(organizationId);
    }

    public VolunteerOrganizationEntity addVolunteerOrganization(VolunteerOrganizationEntity volunteerOrganizationEntity) {
        return volunteerOrganizationRepository.save(volunteerOrganizationEntity);
    }

    public boolean existsByVolunteerIdAndOrganizationId(Integer volunteerId, Integer organizationId) {
        return volunteerOrganizationRepository.existsByVolunteerIdAndOrganizationId(volunteerId, organizationId);
    }

    public VolunteerOrganizationEntity addVolunteerOrganizationPending(VolunteerOrganizationEntity volunteerOrganizationEntity) {
        VolunteerOrganizationEntity savedVolunteerOrganization = volunteerOrganizationRepository.save(volunteerOrganizationEntity);
        postulationService.addPostulationPending(savedVolunteerOrganization.getId());
        dataShareVolunteerOrganizationService.addDataShareVolunteerOrganization(savedVolunteerOrganization.getId());
        return volunteerOrganizationRepository.save(volunteerOrganizationEntity);
    }

    public List<VolunteerOrganizationEntity> findAll () {
        return volunteerOrganizationRepository.findAll();
    }

    public VolunteerOrganizationEntity findByVolunteerIdAndOrganizationId(Integer volunteerId, Integer organizationId) {
        return volunteerOrganizationRepository
                .findByVolunteerIdAndOrganizationId(volunteerId, organizationId)
                .orElseThrow(() -> new EntityNotFoundException("Registro no encontrado con el ID de voluntario: " + volunteerId + " y el ID de organización: " + organizationId));
    }

    public List<Integer> findVolunteerOrganizationIdsByOrganizationId(Integer organizationId) {
        return volunteerOrganizationRepository.findByOrganizationId(organizationId)
                .stream()
                .map(VolunteerOrganizationEntity::getId)
                .toList();
    }

}

