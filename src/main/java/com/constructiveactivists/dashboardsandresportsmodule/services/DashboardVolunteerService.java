package com.constructiveactivists.dashboardsandresportsmodule.services;

import com.constructiveactivists.dashboardsandresportsmodule.controllers.response.CardsOrganizationVolunteerResponse;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.repositories.OrganizationRepository;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupEntity;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupService;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.VolunteerOrganizationService;
import com.constructiveactivists.volunteermodule.services.volunteer.VolunteerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.NOT_FOUND_MESSAGE;
import static com.constructiveactivists.configurationmodule.constants.AppConstants.VOLUNTEER_MESSAGE_ID;

@Service
@AllArgsConstructor
public class DashboardVolunteerService {

    private final VolunteerService volunteerService;

    private final UserService userService;

    private final VolunteerGroupService volunteerGroupService;

    private final ActivityService activityService;

    private final VolunteerOrganizationService volunteerOrganizationService;
    private final OrganizationRepository organizationRepository;
    private final PostulationService postulationService;

    public Map<String, Long> getAgeRanges() {

        return volunteerService.getAllVolunteers().stream()
                .map(volunteer -> volunteerService.calculateAge(volunteer.getPersonalInformation().getBornDate()))
                .collect(Collectors.groupingBy(age -> {
                    if (age > 100) {
                        return "100+";
                    } else {
                        int startRange = (age / 10) * 10;
                        int endRange = startRange + 9;
                        return startRange + "-" + endRange;
                    }
                }, TreeMap::new, Collectors.counting()));
    }

    public double getAverageAge() {
        return volunteerService.getAllVolunteers().stream()
                .mapToInt(volunteer -> volunteer.getPersonalInformation().getAge())
                .average()
                .orElse(0.0);
    }

    public long getActiveVolunteerCount() {
        return volunteerService.getAllVolunteers().stream()
                .filter(volunteer -> {
                    Optional<UserEntity> user = userService.getUserById(volunteer.getUserId());
                    return user.isPresent() && user.get().getAuthorizationType() == AuthorizationStatus.AUTORIZADO;
                })
                .count();
    }

    public Map<SkillEnum, Integer> getSkillCounts() {
        Map<SkillEnum, Integer> skillCountMap = new EnumMap<>(SkillEnum.class);
        volunteerService.getAllVolunteers().forEach(volunteer ->
                volunteer.getVolunteeringInformation().getSkillsList().forEach(skill ->
                        skillCountMap.merge(skill, 1, Integer::sum)
                )
        );
        for (SkillEnum skill : SkillEnum.values()) {
            skillCountMap.putIfAbsent(skill, 0);
        }
        return skillCountMap;
    }

    public Map<AvailabilityEnum, Long> getVolunteerAvailabilityCount() {
        Map<AvailabilityEnum, Long> availabilityCountMap = new EnumMap<>(AvailabilityEnum.class);
        Arrays.stream(AvailabilityEnum.values())
                .forEach(day -> availabilityCountMap.put(day, 0L));

        volunteerService.getAllVolunteers().stream()
                .flatMap(volunteer -> volunteer.getVolunteeringInformation().getAvailabilityDaysList().stream())
                .forEach(day -> availabilityCountMap.merge(day, 1L, Long::sum));

        return availabilityCountMap;
    }

    public Map<InterestEnum, Long> getInterestCount() {
        Map<InterestEnum, Long> interestCountMap = volunteerService.getAllVolunteers().stream()
                .flatMap(volunteer -> volunteer.getVolunteeringInformation().getInterestsList().stream())
                .collect(Collectors.groupingBy(
                        interest -> interest,
                        () -> new EnumMap<>(InterestEnum.class),
                        Collectors.counting()
                ));
        Arrays.stream(InterestEnum.values())
                .filter(interest -> !interestCountMap.containsKey(interest))
                .forEach(interest -> interestCountMap.put(interest, 0L));
        return interestCountMap;
    }

    public ActivityEntity getNextActivityForVolunteer(Integer volunteerId) {
        Optional <VolunteerEntity> volunteer = volunteerService.getVolunteerById(volunteerId);
        if (volunteer.isPresent()) {
            throw new EntityNotFoundException(VOLUNTEER_MESSAGE_ID + volunteerId + NOT_FOUND_MESSAGE);
        }

        List<VolunteerOrganizationEntity> volunteerOrganizationEntities = volunteerOrganizationService.getOrganizationsByVolunteerId(volunteerId);
        if (volunteerOrganizationEntities == null || volunteerOrganizationEntities.isEmpty()) {
            return null;
        }
        Set<Integer> organizationIds = volunteerOrganizationEntities.stream()
                .map(vo -> vo.getOrganizationId())
                .collect(Collectors.toSet());
        List<VolunteerGroupEntity> volunteerGroups = organizationIds.stream()
                .flatMap(orgId -> volunteerGroupService.getVolunteerGroupByOrganizationId(orgId).stream())
                .toList();
        if (volunteerGroups.isEmpty()) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        return volunteerGroups.stream()
                .map(VolunteerGroupEntity::getActivity)
                .map(activityService::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(activity -> {
                    LocalDateTime activityDateTime = LocalDateTime.of(activity.getDate(), activity.getStartTime());
                    return activityDateTime.isAfter(now);
                })
                .min(Comparator.comparing(activity -> LocalDateTime.of(activity.getDate(), activity.getStartTime())))
                .orElse(null);
    }

    // Método para obtener las fundaciones con todos los datos necesarios
    public List<CardsOrganizationVolunteerResponse> getFoundationsByVolunteerId(Integer volunteerId) {
        // Paso 1: Obtener las organizaciones a las que pertenece el voluntario
        List<VolunteerOrganizationEntity> volunteerOrganizations = volunteerOrganizationService.getOrganizationsByVolunteerId(volunteerId);

        // Paso 2: Recorrer las organizaciones y filtrar solo aquellas que están aceptadas
        return volunteerOrganizations.stream()
                .map(volunteerOrg -> {
                    // Obtener la organización
                    Optional<OrganizationEntity> organization = organizationRepository.findById(volunteerOrg.getOrganizationId());

                    // Verificar si la organización está presente
                    if (organization.isPresent()) {
                        // Obtener el usuario asociado a la organización
                        Optional<UserEntity> userEntity = userService.getUserById(organization.get().getUserId());
                        String organizationPhoto = userEntity.map(UserEntity::getImage).orElse(null);

                        // Obtener el estado de postulación
                        Optional<PostulationEntity> postulation = Optional.ofNullable(postulationService.findById(volunteerOrg.getId()));

                        // Verificar si la postulación está aceptada
                        if (postulation.map(PostulationEntity::getStatus).orElse(null) == OrganizationStatusEnum.ACEPTADO) {
                            long authorizedVolunteers = this.countAuthorizedVolunteersByVolunteerId(volunteerId);

                            // Retornar la respuesta
                            return new CardsOrganizationVolunteerResponse(
                                    organization.get().getId(),
                                    organization.get().getOrganizationName(),
                                    organization.get().getDescription(),
                                    organization.get().getOrganizationTypeEnum(),
                                    organizationPhoto,
                                    authorizedVolunteers,
                                    organization.get().getSectorTypeEnum()
                            );
                        }
                    }
                    return null; // Retornar null si la organización no está presente o no está aceptada
                })
                .filter(Objects::nonNull) // Filtrar los resultados nulos
                .toList(); // Convertir a lista
    }


    public long countAuthorizedVolunteersByVolunteerId(Integer volunteerId) {
        List<Integer> organizationIds = volunteerOrganizationService.getOrganizationIdsByVolunteerId(volunteerId);
        return postulationService.countAuthorizedVolunteersByOrganizationIds(organizationIds);
    }




}
