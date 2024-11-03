package com.constructiveactivists.volunteermodule.services.volunteer;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.MissionRepository;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import com.constructiveactivists.missionandactivitymodule.services.mission.MissionService;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupMembershipService;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupService;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.repositories.OrganizationRepository;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.entities.volunteer.PersonalInformationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteeringInformationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.*;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.models.RankedOrganization;
import com.constructiveactivists.volunteermodule.repositories.VolunteerOrganizationRepository;
import com.constructiveactivists.volunteermodule.repositories.VolunteerRepository;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.VolunteerOrganizationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.VOLUNTEER_NOT_FOUND;

@Service
@AllArgsConstructor
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final UserService userService;
    private final VolunteerGroupService volunteerGroupService;
    private final VolunteerGroupMembershipService volunteerGroupMembershipService;
    private final VolunteerOrganizationService volunteerOrganizationService;
    private final MissionService missionService;
    private final ActivityService activityService;
    private final MissionRepository missionRepository;
    private final OrganizationRepository organizationRepository;
    private final VolunteerOrganizationRepository volunteerOrganizationRepository;
    private final PostulationService postulationService;

    private static final int MINIMUM_AGE = 16;
    private static final int MAXIMUM_AGE = 140;

    public List<VolunteerEntity> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    public Optional<VolunteerEntity> getVolunteerById(Integer id) {
        Optional<VolunteerEntity> volunteerOpt = volunteerRepository.findById(id);
        if (volunteerOpt.isPresent()) {
            VolunteerEntity volunteer = volunteerOpt.get();
            LocalDate birthDate = volunteer.getPersonalInformation().getBornDate();
            volunteer.getPersonalInformation().setAge(calculateAge(birthDate));
            volunteerRepository.save(volunteer);
        }
        return volunteerOpt;
    }

    public VolunteerEntity saveVolunteer(VolunteerEntity volunteerEntity) {
        validateUserExists(volunteerEntity.getUserId());
        validateAge(volunteerEntity.getPersonalInformation().getBornDate());
        int age = calculateAge(volunteerEntity.getPersonalInformation().getBornDate());
        volunteerEntity.getPersonalInformation().setAge(age);
        volunteerEntity.getVolunteeringInformation().setVolunteeredTotalHours(0);
        volunteerEntity.getVolunteeringInformation().setRegistrationDate(LocalDateTime.now());
        userService.updateUserRoleType(volunteerEntity.getUserId(), RoleType.VOLUNTARIO);
        volunteerEntity.getVolunteeringInformation().setVolunteerType(VolunteerType.valueOf("VOLUNTARIO"));
        return volunteerRepository.save(volunteerEntity);
    }

    public int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public void deleteVolunteer(Integer id) {
        VolunteerEntity volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El voluntario con ID: " + id + " no existe en la base de datos"));

        userService.deleteUser(volunteer.getUserId());

        volunteerRepository.delete(volunteer);
    }

    public List<InterestEnum> getAllInterests() {
        return Arrays.asList(InterestEnum.values());
    }

    public List<SkillEnum> getAllSkills() {
        return Arrays.asList(SkillEnum.values());
    }

    public List<AvailabilityEnum> getAllAvailabilities() {
        return Arrays.asList(AvailabilityEnum.values());
    }

    public List <RelationshipEnum> getAllRelationships() {
        return Arrays.asList(RelationshipEnum.values());
    }

    public VolunteerEntity updateVolunteer(Integer id, VolunteerEntity entity) {
        VolunteerEntity volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(VOLUNTEER_NOT_FOUND + id ));

        PersonalInformationEntity personalInfo = volunteer.getPersonalInformation();
        PersonalInformationEntity newPersonalInfo = entity.getPersonalInformation();
        personalInfo.setPhoneNumber(newPersonalInfo.getPhoneNumber());
        personalInfo.setAddress(newPersonalInfo.getAddress());
        personalInfo.setBornDate(newPersonalInfo.getBornDate());
        validateAge(personalInfo.getBornDate());
        personalInfo.setAge(calculateAge(personalInfo.getBornDate()));
        VolunteeringInformationEntity volunteeringInfo = volunteer.getVolunteeringInformation();
        VolunteeringInformationEntity newVolunteeringInfo = entity.getVolunteeringInformation();
        newVolunteeringInfo.setVolunteerType(volunteeringInfo.getVolunteerType());
        volunteeringInfo.setVolunteerType(newVolunteeringInfo.getVolunteerType());
        volunteer.setEmergencyInformation(entity.getEmergencyInformation());
        return volunteerRepository.save(volunteer);
    }

    public Optional<VolunteerEntity> getVolunteerByUserId(Integer userId) {
        return volunteerRepository.findByUserId(userId);
    }

    public VolunteerEntity promoteToLeader(Integer volunteerId) {
        VolunteerEntity volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new EntityNotFoundException("El voluntario con ID " + volunteerId + " no existe en la base de datos."));

        if (volunteer.getVolunteeringInformation().getVolunteerType() == VolunteerType.LIDER) {
            return volunteer;
        }

        volunteer.getVolunteeringInformation().setVolunteerType(VolunteerType.LIDER);
        return volunteerRepository.save(volunteer);
    }

    public void signUpForActivity(Integer volunteerId, Integer activityId) {

        VolunteerGroupEntity volunteerGroup = volunteerGroupService.getVolunteerGroupByActivityId(activityId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró un grupo de voluntarios para la actividad con ID: " + activityId));

        if (volunteerGroup.getCurrentVolunteers() >= volunteerGroup.getNumberOfVolunteersRequired()) {
            throw new BusinessException("El grupo de voluntarios ya ha alcanzado la cantidad máxima de voluntarios requeridos.");
        }

        ActivityEntity activity = activityService.getById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la actividad con ID: " + activityId));

        if (activity.getVisibility() == VisibilityEnum.PRIVADA) {

            MissionEntity mission = missionService.getMissionById(activity.getMissionId())
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró la misión con ID: " + activity.getMissionId()));

            Integer missionOrganizationId = mission.getOrganizationId();

            List<VolunteerOrganizationEntity> volunteerOrganizations = volunteerOrganizationService.getOrganizationsByVolunteerId(volunteerId);

            boolean isVolunteerInOrganization = volunteerOrganizations.stream()
                    .anyMatch(org -> org.getOrganizationId().equals(missionOrganizationId));

            if (!isVolunteerInOrganization) {
                throw new BusinessException("El voluntario no pertenece a la organización requerida para inscribirse en esta actividad privada.");
            }
        }

        boolean alreadyMember = volunteerGroupMembershipService.isVolunteerInGroup(volunteerGroup.getId(), volunteerId);
        if (alreadyMember) {
            throw new BusinessException("El voluntario ya está registrado en el grupo.");
        }

        volunteerGroupMembershipService.addVolunteerToGroup(volunteerGroup.getId(), volunteerId);

        volunteerGroup.setCurrentVolunteers(volunteerGroup.getCurrentVolunteers() + 1);
        volunteerGroupService.save(volunteerGroup);
    }

    public void removeVolunteerFromActivity(Integer volunteerId, Integer activityId) {

        VolunteerGroupEntity volunteerGroup = volunteerGroupService.getVolunteerGroupByActivityId(activityId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró un grupo de voluntarios para la actividad con ID: " + activityId));

        ActivityEntity activity = activityService.getById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la actividad con ID: " + activityId));

        LocalDate today = LocalDate.now();
        if (activity.getDate().isBefore(today.plusDays(2))) {
            throw new BusinessException("Solo puedes eliminar la inscripción hasta 2 días antes del inicio de la actividad.");
        }

        boolean isMember = volunteerGroupMembershipService.isVolunteerInGroup(volunteerGroup.getId(), volunteerId);
        if (!isMember) {
            throw new BusinessException("El voluntario no está registrado en el grupo.");
        }

        volunteerGroupMembershipService.removeVolunteerFromGroup(volunteerGroup.getId(), volunteerId);
        volunteerGroup.setCurrentVolunteers(volunteerGroup.getCurrentVolunteers() - 1);
        volunteerGroupService.save(volunteerGroup);
    }

    void validateUserExists(Integer userId) {
        Optional<UserEntity> user = userService.getUserById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("El usuario con ID " + userId + " no existe en la bd");
        }
        if(!user.get().getRole().getRoleType().equals(RoleType.SIN_ASIGNAR)){
            throw new BusinessException("El usuario ya tiene un rol asignado");
        }
    }

    public void addVolunteerHours(Integer volunteerId, int hoursToAdd) {
        VolunteerEntity volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new EntityNotFoundException("Volunteer not found with ID: " + volunteerId));

        VolunteeringInformationEntity volunteeringInfo = volunteer.getVolunteeringInformation();
        int updatedHours = volunteeringInfo.getVolunteeredTotalHours() + hoursToAdd;
        volunteeringInfo.setVolunteeredTotalHours(updatedHours);
        volunteerRepository.save(volunteer);
    }

    public void addVolunteerActivity(Integer volunteerId, Integer activityId) {
        VolunteerEntity volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new EntityNotFoundException("Volunteer not found with ID: " + volunteerId));

        VolunteeringInformationEntity volunteeringInfo = volunteer.getVolunteeringInformation();
        List<Integer> activitiesCompleted = volunteeringInfo.getActivitiesCompleted();
        if (activitiesCompleted == null) {
            activitiesCompleted = new ArrayList<>();
        }
        activitiesCompleted.add(activityId);
        volunteeringInfo.setActivitiesCompleted(activitiesCompleted);
        volunteerRepository.save(volunteer);
    }

    private void validateAge(LocalDate birthDate) {
        int age = calculateAge(birthDate);
        if (age < MINIMUM_AGE) {
            throw new BusinessException("El voluntario debe tener al menos " + MINIMUM_AGE + " años.");
        } else if (age > MAXIMUM_AGE) {
            throw new BusinessException("La edad del voluntario no puede exceder los " + MAXIMUM_AGE + " años.");
        }
    }

    public List<VolunteerEntity> getVolunteersByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return volunteerRepository.findByRegistrationYear(startDateTime, endDateTime);
    }

    public List<RankedOrganization> matchVolunteerWithMissions(Integer volunteerId, int cantidadMatch) {

        VolunteerEntity volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new EntityNotFoundException("El voluntario con ID " + volunteerId + " no existe."));

        List<InterestEnum> volunteerInterests = volunteer.getVolunteeringInformation().getInterestsList();
        List<SkillEnum> volunteerSkills = volunteer.getVolunteeringInformation().getSkillsList();

        List<MissionEntity> matchingMissions = missionRepository.findMissionsByInterestsAndSkills(volunteerInterests, volunteerSkills);

        if (matchingMissions.isEmpty()) {
            return Collections.emptyList();
        }

        Map<MissionEntity, Integer> missionMatchScores = calculateMissionMatchScores(volunteerInterests, volunteerSkills, matchingMissions);

        Map<OrganizationEntity, Integer> organizationScores = new HashMap<>();

        for (Map.Entry<MissionEntity, Integer> entry : missionMatchScores.entrySet()) {
            MissionEntity mission = entry.getKey();
            Integer score = entry.getValue();

            OrganizationEntity organization = organizationRepository.findById(mission.getOrganizationId())
                    .orElseThrow(() -> new EntityNotFoundException("Organización no encontrada con ID: " + mission.getOrganizationId()));

            organizationScores.merge(organization, score, Integer::sum);
        }

        List<Integer> existingOrganizationIds = volunteerOrganizationRepository.findByVolunteerId(volunteerId)
                .stream()
                .map(VolunteerOrganizationEntity::getOrganizationId)
                .toList();

        List<RankedOrganization> rankedOrganizations = organizationScores.entrySet().stream()
                .filter(entry -> !existingOrganizationIds.contains(entry.getKey().getId()))
                .map(entry -> {
                    OrganizationEntity organization = entry.getKey();
                    int score = entry.getValue();

                    String photoUrl = userService.getUserById(organization.getUserId())
                            .map(UserEntity::getImage)
                            .orElse(null);

                    long authorizedVolunteersCount = countAuthorizedVolunteersByOrganizationId(organization.getId());

                    return new RankedOrganization(organization, score, photoUrl, authorizedVolunteersCount);
                })
                .sorted(Comparator.comparingInt(RankedOrganization::getScore).reversed())
                .limit(cantidadMatch)
                .toList();

        rankedOrganizations = breakTiesRandomly(rankedOrganizations);

        return rankedOrganizations;
    }

    public long countAuthorizedVolunteersByOrganizationId(Integer organizationId) {
        Map<Integer, Long> acceptedVolunteersCount = postulationService.countAcceptedVolunteers();
        return acceptedVolunteersCount.getOrDefault(organizationId, 0L);
    }


    Map<MissionEntity, Integer> calculateMissionMatchScores(List<InterestEnum> volunteerInterests,
                                                            List<SkillEnum> volunteerSkills,
                                                            List<MissionEntity> missions) {
        Map<MissionEntity, Integer> missionMatchScores = new HashMap<>();

        for (MissionEntity mission : missions) {
            int score = 0;

            for (InterestEnum interest : volunteerInterests) {
                if (mission.getRequiredInterestsList().contains(interest)) {
                    score += 3;
                }
            }

            for (SkillEnum skill : volunteerSkills) {
                if (mission.getRequiredSkillsList().contains(skill)) {
                    score += 2;
                }
            }

            missionMatchScores.put(mission, score);
        }

        return missionMatchScores;
    }

    List<RankedOrganization> breakTiesRandomly(List<RankedOrganization> rankedOrganizations) {
        Map<Integer, List<RankedOrganization>> groupedByScore = rankedOrganizations.stream()
                .collect(Collectors.groupingBy(RankedOrganization::getScore));

        List<RankedOrganization> finalRanking = new ArrayList<>();

        List<Integer> sortedScores = groupedByScore.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .toList();

        for (Integer score : sortedScores) {
            List<RankedOrganization> organizationsWithSameScore = groupedByScore.get(score);
            Collections.shuffle(organizationsWithSameScore);
            finalRanking.addAll(organizationsWithSameScore);
        }

        return finalRanking;
    }

    public void addCompletedActivity(Integer volunteerId, Integer activityId) {
        VolunteerEntity volunteerEntity = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new IllegalArgumentException("Voluntario no encontrado"));
        VolunteeringInformationEntity volunteeringInfo = volunteerEntity.getVolunteeringInformation();
        if (!volunteeringInfo.getActivitiesCompleted().contains(activityId)) {
            volunteeringInfo.getActivitiesCompleted().add(activityId);
            volunteerRepository.save(volunteerEntity);
        }
    }



}
