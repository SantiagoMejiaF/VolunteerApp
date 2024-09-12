package com.constructiveactivists.volunteermodule.services.volunteerorganization;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.organizationmodule.repositories.OrganizationRepository;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.repositories.VolunteerOrganizationRepository;
import com.constructiveactivists.volunteermodule.repositories.VolunteerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VolunteerOrganizationService {

    private final VolunteerOrganizationRepository volunteerOrganizationRepository;
    private final PostulationService postulationService;
    private final DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;
    private final VolunteerRepository volunteerRepository;
    private final OrganizationRepository organizationRepository;

    public List<VolunteerOrganizationEntity> getOrganizationsByVolunteerId(Integer volunteerId) {
        return volunteerOrganizationRepository.findByVolunteerId(volunteerId);
    }

    public List<VolunteerOrganizationEntity> getVolunteersByOrganizationId(Integer organizationId) {
        return volunteerOrganizationRepository.findByOrganizationId(organizationId);
    }

    public List<VolunteerOrganizationEntity> findAll () {
        return volunteerOrganizationRepository.findAll();
    }

    public List<Integer> findVolunteerOrganizationIdsByOrganizationId(Integer organizationId) {
        return volunteerOrganizationRepository.findByOrganizationId(organizationId)
                .stream()
                .map(VolunteerOrganizationEntity::getId)
                .toList();
    }

    public VolunteerOrganizationEntity save(VolunteerOrganizationEntity volunteerOrganization) {
        boolean volunteerExists = volunteerRepository.existsById(volunteerOrganization.getVolunteerId());
        if (!volunteerExists) {
            throw new EntityNotFoundException("El voluntario con ID " + volunteerOrganization.getVolunteerId() + " no existe.");
        }
        boolean organizationExists = organizationRepository.existsById(volunteerOrganization.getOrganizationId());
        if (!organizationExists) {
            throw new EntityNotFoundException("La organización con ID " + volunteerOrganization.getOrganizationId() + " no existe.");
        }
        return volunteerOrganizationRepository.save(volunteerOrganization);
    }

    public VolunteerOrganizationEntity addVolunteerOrganizationPending(VolunteerOrganizationEntity volunteerOrganizationEntity) {
        if(volunteerOrganizationRepository.existsByVolunteerIdAndOrganizationId(volunteerOrganizationEntity.getVolunteerId(), volunteerOrganizationEntity.getOrganizationId())) {
            throw new BusinessException("El voluntario ya está registrado en la organización");
        }
        VolunteerOrganizationEntity savedVolunteerOrganization = this.save(volunteerOrganizationEntity);
        postulationService.addPostulationPending(savedVolunteerOrganization.getId());
        dataShareVolunteerOrganizationService.addDataShareVolunteerOrganization(savedVolunteerOrganization.getId());
        return savedVolunteerOrganization;
    }

    public VolunteerOrganizationEntity findByVolunteerIdAndOrganizationId(Integer volunteerId, Integer organizationId) {
        return volunteerOrganizationRepository.findByVolunteerIdAndOrganizationId(volunteerId, organizationId)
                .orElseThrow(() -> new EntityNotFoundException("La relación entre el voluntario con ID " + volunteerId + " y la organización con ID " + organizationId + " no se encuentra."));
    }

    public boolean existsByVolunteerIdAndOrganizationId(Integer volunteerId, Integer organizationId) {
        return volunteerOrganizationRepository.existsByVolunteerIdAndOrganizationId(volunteerId, organizationId);
    }

}
