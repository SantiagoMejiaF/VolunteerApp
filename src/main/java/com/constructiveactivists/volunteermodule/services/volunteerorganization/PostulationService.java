package com.constructiveactivists.volunteermodule.services.volunteerorganization;

import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.repositories.PostulationRepository;
import com.constructiveactivists.volunteermodule.repositories.VolunteerOrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.POSTULATION_NOT_FOUND;
import static com.constructiveactivists.configurationmodule.constants.AppConstants.ZONE_PLACE;

@Service
@AllArgsConstructor
public class PostulationService {

    private final PostulationRepository postulationRepository;
    private final VolunteerOrganizationRepository volunteerOrganizationRepository;
    private final DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;

    public PostulationEntity addPostulationPending(Integer volunteerOrganizationId) {
        PostulationEntity postulationEntity = new PostulationEntity();
        postulationEntity.setVolunteerOrganizationId(volunteerOrganizationId);
        postulationEntity.setStatus(OrganizationStatusEnum.PENDIENTE);
        ZonedDateTime bogotaDateTime = ZonedDateTime.now(ZoneId.of(ZONE_PLACE));
        postulationEntity.setRegistrationDate(bogotaDateTime.toLocalDate());
        return postulationRepository.save(postulationEntity);
    }

    public void updateStatusAccept(Integer volunteerOrganizationId) {
        PostulationEntity postulation = postulationRepository.findById(volunteerOrganizationId)
                .orElseThrow(() -> new EntityNotFoundException(POSTULATION_NOT_FOUND + volunteerOrganizationId));
        ZonedDateTime bogotaDateTime = ZonedDateTime.now(ZoneId.of(ZONE_PLACE));
        postulation.setRegistrationDate(bogotaDateTime.toLocalDate());
        postulation.setStatus(OrganizationStatusEnum.ACEPTADO);
        postulationRepository.save(postulation);
    }

    public void updateStatusRefuse(Integer volunteerOrganizationId) {
        PostulationEntity postulation = postulationRepository.findById(volunteerOrganizationId)
                .orElseThrow(() -> new EntityNotFoundException(POSTULATION_NOT_FOUND + volunteerOrganizationId));
        ZonedDateTime bogotaDateTime = ZonedDateTime.now(ZoneId.of(ZONE_PLACE));
        postulation.setRegistrationDate(bogotaDateTime.toLocalDate());
        postulation.setStatus(OrganizationStatusEnum.RECHAZADO);
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

    public List<PostulationEntity> getAcceptedPostulationsByOrganizationId(Integer organizationId) {
        List<VolunteerOrganizationEntity> volunteerOrganizations = volunteerOrganizationRepository.findByOrganizationId(organizationId);
        List<Integer> volunteerOrganizationIds = volunteerOrganizations.stream()
                .map(VolunteerOrganizationEntity::getId)
                .toList();
        if (volunteerOrganizationIds.isEmpty()) {
            return Collections.emptyList();
        }
        return postulationRepository.findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum.ACEPTADO, volunteerOrganizationIds);
    }

    public List<PostulationEntity> getRefusedPostulationsByOrganizationId(Integer organizationId) {
        List<VolunteerOrganizationEntity> volunteerOrganizations = volunteerOrganizationRepository.findByOrganizationId(organizationId);
        List<Integer> volunteerOrganizationIds = volunteerOrganizations.stream()
                .map(VolunteerOrganizationEntity::getId)
                .toList();
        if (volunteerOrganizationIds.isEmpty()) {
            return Collections.emptyList();
        }
        return postulationRepository.findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum.RECHAZADO, volunteerOrganizationIds);
    }

    public PostulationEntity findById(Integer volunteerOrganizationId) {
        return postulationRepository.findById(volunteerOrganizationId)
                .orElseThrow(() -> new EntityNotFoundException(POSTULATION_NOT_FOUND  + volunteerOrganizationId));
    }

    public List<PostulationEntity> getPostulationsByDateRange(LocalDate startDate, LocalDate endDate) {
        return postulationRepository.findAllByRegistrationDateBetween(startDate, endDate);
    }

    public List<PostulationEntity> findAll() {
        return postulationRepository.findAll();
    }

    public void addTimeToPostulation(Integer volunteerOrganizationId, Integer hours) {
        PostulationEntity postulation = postulationRepository.findById(volunteerOrganizationId)
                .orElseThrow(() -> new EntityNotFoundException(POSTULATION_NOT_FOUND + volunteerOrganizationId));
        DataShareVolunteerOrganizationEntity datashare = dataShareVolunteerOrganizationService.findById(postulation.getVolunteerOrganizationId());
        datashare.setHoursDone(datashare.getHoursDone() + hours);
        datashare.setHoursCertified(datashare.getHoursCertified() + hours);
        datashare.setMonthlyHours(datashare.getMonthlyHours() + hours);
        postulationRepository.save(postulation);
    }

    public Map<Integer, Long> countAcceptedVolunteers() {
        List<Object[]> results = postulationRepository.countAcceptedVolunteersByOrganization(OrganizationStatusEnum.ACEPTADO);
        return results.stream()
                .collect(Collectors.toMap(
                        result -> (Integer) result[0],
                        result -> (Long) result[1]
                ));
    }
}
