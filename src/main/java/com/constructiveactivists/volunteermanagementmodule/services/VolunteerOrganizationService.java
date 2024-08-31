package com.constructiveactivists.volunteermanagementmodule.services;

import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.OrganizationStatusEnum;
import com.constructiveactivists.volunteermanagementmodule.repositories.VolunteerOrganizationRepository;
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

    public VolunteerOrganizationEntity addVolunteerOrganization(VolunteerOrganizationEntity volunteerOrganizationEntity) {
        return volunteerOrganizationRepository.save(volunteerOrganizationEntity);
    }

    public VolunteerOrganizationEntity updateHours(Integer id, Integer hoursCompleted, Integer hoursCertified) {
        VolunteerOrganizationEntity volunteerOrganizationEntity = volunteerOrganizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro no encontrado con ID: " + id));
        volunteerOrganizationEntity.setHoursDone(hoursCompleted);
        volunteerOrganizationEntity.setHoursCertified(hoursCertified);
        return volunteerOrganizationRepository.save(volunteerOrganizationEntity);
    }

    public boolean existsByVolunteerIdAndOrganizationId(Integer volunteerId, Integer organizationId) {
        return volunteerOrganizationRepository.existsByVolunteerIdAndOrganizationId(volunteerId, organizationId);
    }

    public List<VolunteerOrganizationEntity> getVolunteersWithStatus(OrganizationStatusEnum status) {
        return volunteerOrganizationRepository.findByStatus(status);
    }

    public VolunteerOrganizationEntity addVolunteerOrganizationPending(VolunteerOrganizationEntity volunteerOrganizationEntity) {
        volunteerOrganizationEntity.setStatus(OrganizationStatusEnum.PENDIENTE);
        volunteerOrganizationEntity.setHoursDone(0);
        volunteerOrganizationEntity.setHoursCertified(0);
        volunteerOrganizationEntity.setMonthlyHours(0);
        volunteerOrganizationEntity.setRegistrationDate(java.time.LocalDate.now());
        return volunteerOrganizationRepository.save(volunteerOrganizationEntity);
    }

    public void updateStatusAccept (Integer volunteerId, Integer organizationId) {
        VolunteerOrganizationEntity volunteerOrganizationEntity = volunteerOrganizationRepository.findByVolunteerIdAndOrganizationId(volunteerId, organizationId)
                .orElseThrow(() -> new EntityNotFoundException("Registro no encontrado con ID: " + volunteerId));
        volunteerOrganizationEntity.setStatus(OrganizationStatusEnum.ACEPTADO);
        volunteerOrganizationRepository.save(volunteerOrganizationEntity);
    }

    public List<VolunteerOrganizationEntity> findAll () {
        return volunteerOrganizationRepository.findAll();
    }

    public VolunteerOrganizationEntity findByVolunteerIdAndOrganizationId(Integer volunteerId, Integer organizationId) {
        return volunteerOrganizationRepository
                .findByVolunteerIdAndOrganizationId(volunteerId, organizationId)
                .orElseThrow(() -> new EntityNotFoundException("Registro no encontrado con el ID de voluntario: " + volunteerId + " y el ID de organización: " + organizationId));
    }
}

