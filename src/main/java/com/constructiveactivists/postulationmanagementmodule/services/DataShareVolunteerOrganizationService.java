package com.constructiveactivists.postulationmanagementmodule.services;

import com.constructiveactivists.postulationmanagementmodule.entities.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.postulationmanagementmodule.repositories.DataShareVolunteerOrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DataShareVolunteerOrganizationService {

    private final DataShareVolunteerOrganizationRepository dataShareVolunteerOrganizationRepository;

    public DataShareVolunteerOrganizationEntity updateHours(Integer volunteerOrganizationId, Integer hoursDone, Integer hoursCertified, Integer monthlyHours) {
        DataShareVolunteerOrganizationEntity dataShare = dataShareVolunteerOrganizationRepository.findById(volunteerOrganizationId)
                .orElseThrow(() -> new EntityNotFoundException("Registro no encontrado con ID: " + volunteerOrganizationId));
        dataShare.setHoursDone(hoursDone);
        dataShare.setHoursCertified(hoursCertified);
        dataShare.setMonthlyHours(monthlyHours);
        return dataShareVolunteerOrganizationRepository.save(dataShare);
    }

    public  DataShareVolunteerOrganizationEntity addDataShareVolunteerOrganization( Integer volunteerOrganizationId) {
        DataShareVolunteerOrganizationEntity dataShareVolunteerOrganizationEntity = new DataShareVolunteerOrganizationEntity();
        dataShareVolunteerOrganizationEntity.setVolunteerOrganizationId(volunteerOrganizationId);
        dataShareVolunteerOrganizationEntity.setHoursDone(0);
        dataShareVolunteerOrganizationEntity.setHoursCertified(0);
        dataShareVolunteerOrganizationEntity.setMonthlyHours(0);
        return dataShareVolunteerOrganizationRepository.save(dataShareVolunteerOrganizationEntity);
    }

    public DataShareVolunteerOrganizationEntity findById(Integer volunteerOrganizationId) {
        return dataShareVolunteerOrganizationRepository.findById(volunteerOrganizationId)
                .orElseThrow(() -> new EntityNotFoundException("Registro no encontrado con ID: " + volunteerOrganizationId));
    }


    public List<DataShareVolunteerOrganizationEntity> findAll() {
        return dataShareVolunteerOrganizationRepository.findAll();
    }

    public List<DataShareVolunteerOrganizationEntity> findAllByVolunteerOrganizationIdIn(List<Integer> ids) {
        return dataShareVolunteerOrganizationRepository.findAllByVolunteerOrganizationIdIn(ids);
    }

}

