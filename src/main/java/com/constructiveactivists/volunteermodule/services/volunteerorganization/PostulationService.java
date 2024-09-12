package com.constructiveactivists.volunteermodule.services.volunteerorganization;


import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.repositories.PostulationRepository;
import com.constructiveactivists.volunteermodule.repositories.VolunteerOrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class PostulationService {

    private final PostulationRepository postulationRepository;
    private final VolunteerOrganizationRepository volunteerOrganizationRepository;

    public PostulationEntity addPostulationPending(Integer volunteerOrganizationId) {
        PostulationEntity postulationEntity = new PostulationEntity();
        postulationEntity.setVolunteerOrganizationId(volunteerOrganizationId);
        postulationEntity.setStatus(OrganizationStatusEnum.PENDIENTE);
        postulationEntity.setRegistrationDate(LocalDate.now());
        return postulationRepository.save(postulationEntity);
    }

    public void updateStatusAccept(Integer volunteerOrganizationId) {
        PostulationEntity postulation = postulationRepository.findById(volunteerOrganizationId)
                .orElseThrow(() -> new EntityNotFoundException("Registro no encontrado con ID: " + volunteerOrganizationId));
        postulation.setRegistrationDate(LocalDate.now());
        postulation.setStatus(OrganizationStatusEnum.ACEPTADO);
        postulationRepository.save(postulation);
    }

    public List<PostulationEntity> getPendingPostulationsByOrganizationId(Integer organizationId) {
        List<VolunteerOrganizationEntity> volunteerOrganizations = volunteerOrganizationRepository.findByOrganizationId(organizationId);
        List<Integer> volunteerOrganizationIds = volunteerOrganizations.stream()
                .map(VolunteerOrganizationEntity::getId)
                .toList();
        if (volunteerOrganizationIds.isEmpty()) {
            return Collections.emptyList();
        }
        return postulationRepository.findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum.PENDIENTE, volunteerOrganizationIds);
    }

    public PostulationEntity findById(Integer volunteerOrganizationId) {
        return postulationRepository.findById(volunteerOrganizationId)
                .orElseThrow(() -> new EntityNotFoundException("Registro no encontrado con ID: " + volunteerOrganizationId));
    }

    public List<PostulationEntity> getPostulationsByDateRange(LocalDate startDate, LocalDate endDate) {
        return postulationRepository.findAllByRegistrationDateBetween(startDate, endDate);
    }

    public List<PostulationEntity> findAll() {
        return postulationRepository.findAll();
    }

}
