package com.constructiveactivists.volunteermodule.services.volunteerorganization;

import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.repositories.DataShareVolunteerOrganizationRepository;
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

    public void addDataShareVolunteerOrganization(Integer volunteerOrganizationId) {
        DataShareVolunteerOrganizationEntity dataShareVolunteerOrganizationEntity = new DataShareVolunteerOrganizationEntity();
        dataShareVolunteerOrganizationEntity.setVolunteerOrganizationId(volunteerOrganizationId);
        dataShareVolunteerOrganizationEntity.setHoursDone(0);
        dataShareVolunteerOrganizationEntity.setHoursCertified(0);
        dataShareVolunteerOrganizationEntity.setMonthlyHours(0);
        dataShareVolunteerOrganizationRepository.save(dataShareVolunteerOrganizationEntity);
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
