package com.constructiveactivists.volunteermodule.services.volunteerorganization;

import com.constructiveactivists.missionandactivitymodule.repositories.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.repositories.OrganizationRepository;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.repositories.UserRepository;
import com.constructiveactivists.volunteermodule.controllers.response.StatusVolunteerOrganizationResponse;
import com.constructiveactivists.volunteermodule.controllers.response.VolunteerOrganizationResponse;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.repositories.DataShareVolunteerOrganizationRepository;
import com.constructiveactivists.volunteermodule.repositories.PostulationRepository;
import com.constructiveactivists.volunteermodule.repositories.VolunteerOrganizationRepository;
import com.constructiveactivists.volunteermodule.repositories.VolunteerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class VolunteerOrganizationService {

    private final VolunteerOrganizationRepository volunteerOrganizationRepository;
    private final PostulationService postulationService;
    private final DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;
    private final VolunteerRepository volunteerRepository;
    private final OrganizationRepository organizationRepository;
    private final PostulationRepository postulationRepository;
    private final DataShareVolunteerOrganizationRepository dataShareRepository;
    private final UserRepository userRepository;

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

    public VolunteerOrganizationResponse getVolunteerOrganizationDetails(Integer volunteerOrganizationId) {
        VolunteerOrganizationEntity volunteerOrganization = volunteerOrganizationRepository
                .findById(volunteerOrganizationId)
                .orElseThrow(() -> new BusinessException("VolunteerOrganization not found"));

        PostulationEntity postulation = postulationRepository
                .findById(volunteerOrganizationId)
                .orElseThrow(() -> new BusinessException("Postulation not found"));

        DataShareVolunteerOrganizationEntity dataShare = dataShareRepository
                .findById(volunteerOrganizationId)
                .orElseThrow(() -> new BusinessException("DataShare not found"));

        VolunteerEntity volunteer = volunteerRepository
                .findById(volunteerOrganization.getVolunteerId())
                .orElseThrow(() -> new BusinessException("Volunteer not found"));

        VolunteerOrganizationResponse detailsDTO = new VolunteerOrganizationResponse();
        detailsDTO.setId(volunteerOrganization.getId());
        detailsDTO.setVolunteerId(volunteerOrganization.getVolunteerId());
        detailsDTO.setOrganizationId(volunteerOrganization.getOrganizationId());
        detailsDTO.setStatus(postulation.getStatus());
        detailsDTO.setRegistrationDate(postulation.getRegistrationDate());
        detailsDTO.setHoursDone(dataShare.getHoursDone());
        detailsDTO.setHoursCertified(dataShare.getHoursCertified());
        detailsDTO.setMonthlyHours(dataShare.getMonthlyHours());
        detailsDTO.setUserId(volunteer.getUserId());
        detailsDTO.setVisibility(volunteer.getVisibility());
        detailsDTO.setPersonalInformation(volunteer.getPersonalInformation());
        detailsDTO.setVolunteeringInformation(volunteer.getVolunteeringInformation());
        detailsDTO.setEmergencyInformation(volunteer.getEmergencyInformation());

        return detailsDTO;
    }

    public List<StatusVolunteerOrganizationResponse> getPendingVolunteersByOrganizationId(Integer organizationId) {
        return getVolunteersByOrganizationIdAndStatus(organizationId, OrganizationStatusEnum.PENDIENTE);
    }

    public List<StatusVolunteerOrganizationResponse> getAcceptedVolunteersByOrganizationId(Integer organizationId) {
        return getVolunteersByOrganizationIdAndStatus(organizationId, OrganizationStatusEnum.ACEPTADO);
    }

    public List<StatusVolunteerOrganizationResponse> getRejectedVolunteersByOrganizationId(Integer organizationId) {
        return getVolunteersByOrganizationIdAndStatus(organizationId, OrganizationStatusEnum.RECHAZADO);
    }

    List<StatusVolunteerOrganizationResponse> getVolunteersByOrganizationIdAndStatus(Integer organizationId, OrganizationStatusEnum status) {
        List<Integer> volunteerOrganizationIds = volunteerOrganizationRepository.findByOrganizationId(organizationId)
                .stream()
                .map(VolunteerOrganizationEntity::getId)
                .toList();

        if (volunteerOrganizationIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<PostulationEntity> postulations = postulationRepository.findByStatusAndVolunteerOrganizationIdIn(status, volunteerOrganizationIds);

        return postulations.stream()
                .map(postulation -> {
                    VolunteerEntity volunteer = volunteerRepository
                            .findById(getVolunteerIdByOrganization(postulation.getVolunteerOrganizationId()))
                            .orElseThrow(() -> new BusinessException("Volunteer not found"));

                    UserEntity user = userRepository
                            .findById(volunteer.getUserId())
                            .orElseThrow(() -> new BusinessException("User not found"));

                    return new StatusVolunteerOrganizationResponse(
                            user.getFirstName() + " " + user.getLastName(),
                            user.getEmail(),
                            volunteer.getPersonalInformation().getIdentificationCard(),
                            postulation.getStatus()
                    );
                })
                .toList();
    }

    private Integer getVolunteerIdByOrganization(Integer volunteerOrganizationId) {
        return volunteerOrganizationRepository.findById(volunteerOrganizationId)
                .orElseThrow(() -> new BusinessException("VolunteerOrganization not found"))
                .getVolunteerId();
    }

    public List<Integer> getOrganizationIdsByVolunteerId(Integer volunteerId) {
        List<VolunteerOrganizationEntity> volunteerOrganizations = volunteerOrganizationRepository.findByVolunteerId(volunteerId);
        return volunteerOrganizations.stream()
                .map(VolunteerOrganizationEntity::getOrganizationId)
                .toList();
    }

    public List<OrganizationEntity> getRecentOrganizationsByVolunteerId(Integer volunteerId) {
        List<VolunteerOrganizationEntity> volunteerOrganizations = volunteerOrganizationRepository.findByVolunteerId(volunteerId);
        if (volunteerOrganizations.isEmpty()) {
            return List.of();
        }
        List<Integer> volunteerOrganizationIds = volunteerOrganizations.stream()
                .map(VolunteerOrganizationEntity::getId)
                .toList();
        List<PostulationEntity> recentPostulations = postulationRepository.findByVolunteerOrganizationIdIn(volunteerOrganizationIds);
        if (recentPostulations.isEmpty()) {
            return List.of();
        }
        List<Integer> organizationIds = recentPostulations.stream()
                .sorted(Comparator.comparing(PostulationEntity::getRegistrationDate).reversed())
                .limit(5)
                .map(PostulationEntity::getVolunteerOrganizationId)
                .map(volunteerOrganizationId -> volunteerOrganizationRepository.findById(volunteerOrganizationId)
                        .map(VolunteerOrganizationEntity::getOrganizationId)
                        .orElse(null))
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        return organizationRepository.findAllById(organizationIds);
    }

    public List<VolunteerOrganizationEntity> findAcceptedVolunteerOrganizationsByOrganizationId(Integer organizationId) {
        List<Integer> volunteerOrganizationIds = volunteerOrganizationRepository.findByOrganizationId(organizationId)
                .stream()
                .map(VolunteerOrganizationEntity::getId)
                .toList();
        if (volunteerOrganizationIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<PostulationEntity> acceptedPostulations = postulationRepository.findByStatusAndVolunteerOrganizationIdIn(
                OrganizationStatusEnum.ACEPTADO, volunteerOrganizationIds);
        return acceptedPostulations.stream()
                .map(postulation -> volunteerOrganizationRepository
                        .findById(postulation.getVolunteerOrganizationId())
                        .orElseThrow(() -> new BusinessException("Volunteer organization not found")))
                .toList();
    }


}
