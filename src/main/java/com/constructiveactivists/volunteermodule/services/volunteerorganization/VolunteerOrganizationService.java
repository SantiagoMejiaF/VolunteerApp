package com.constructiveactivists.volunteermodule.services.volunteerorganization;

import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.repositories.VolunteerOrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VolunteerOrganizationService {

    private final VolunteerOrganizationRepository volunteerOrganizationRepository;

    public List<VolunteerOrganizationEntity> getOrganizationsByVolunteerId(Integer volunteerId) {
        return volunteerOrganizationRepository.findByVolunteerId(volunteerId);
    }

    public List<VolunteerOrganizationEntity> getVolunteersByOrganizationId(Integer organizationId) {
        return volunteerOrganizationRepository.findByOrganizationId(organizationId);
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
