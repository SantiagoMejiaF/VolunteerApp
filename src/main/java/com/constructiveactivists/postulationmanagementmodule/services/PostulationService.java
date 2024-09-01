package com.constructiveactivists.postulationmanagementmodule.services;

import com.constructiveactivists.postulationmanagementmodule.entities.PostulationEntity;
import com.constructiveactivists.postulationmanagementmodule.repositories.PostulationRepository;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.OrganizationStatusEnum;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class PostulationService {

    private final PostulationRepository postulationRepository;

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
        postulation.setStatus(OrganizationStatusEnum.ACEPTADO);
        postulationRepository.save(postulation);
    }

    public List<PostulationEntity> getVolunteersWithStatus(OrganizationStatusEnum status) {
        return postulationRepository.findByStatus(status);
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

