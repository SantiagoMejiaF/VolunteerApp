package com.constructiveactivists.volunteermanagementmodule.services;

import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermanagementmodule.entities.enums.RoleType;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import com.constructiveactivists.volunteermanagementmodule.entities.PersonalInformationEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteeringInformationEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.*;
import com.constructiveactivists.volunteermanagementmodule.repositories.VolunteerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(VolunteerService.class);

    private static final int MINIMUM_AGE = 16;

    public List<VolunteerEntity> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    public Optional<VolunteerEntity> getVolunteerById(Integer id) {
        Optional<VolunteerEntity> volunteerOpt = volunteerRepository.findById(id);
        if (volunteerOpt.isPresent()) {
            VolunteerEntity volunteer = volunteerOpt.get();
            LocalDate birthDate = volunteer.getPersonalInformation().getBirthDate();
            volunteer.getPersonalInformation().setAge(calculateAge(birthDate));
            volunteerRepository.save(volunteer);
        }
        return volunteerOpt;
    }


    public VolunteerEntity saveVolunteer(VolunteerEntity volunteerEntity) {
        validateUserExists(volunteerEntity.getUserId());
        validateAge(volunteerEntity.getPersonalInformation().getBirthDate());
        int age = calculateAge(volunteerEntity.getPersonalInformation().getBirthDate());
        volunteerEntity.getPersonalInformation().setAge(age);

        userService.updateUserRoleType(volunteerEntity.getUserId(), RoleType.VOLUNTARIO);
        volunteerEntity.getVolunteeringInformation().setVolunteerType(VolunteerType.valueOf("VOLUNTARIO"));
        volunteerEntity.getVolunteeringInformation().setVolunteeredHours(0);
        return volunteerRepository.save(volunteerEntity);
    }

    private void validateUserExists(Integer userId) {
        Optional<UserEntity> user = userService.getUserById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("El usuario con ID " + userId + " no existe en la bd");
        }
    }

    private void validateAge(LocalDate birthDate) {
        int age = calculateAge(birthDate);
        if (age < MINIMUM_AGE) {
            throw new IllegalArgumentException("El voluntario debe tener al menos " + MINIMUM_AGE +" años.");
        }
    }

    public int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public long getActiveVolunteerCount() {
        return volunteerRepository.findAll().stream()
                .filter(volunteer -> {
                    Optional<UserEntity> user = userService.getUserById(volunteer.getUserId());
                    return user.isPresent() && user.get().getAuthorizationType() == AuthorizationStatus.AUTORIZADO;
                })
                .count();
    }

    public void deleteVolunteer(Integer id) {
        VolunteerEntity volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El voluntario con ID " + id + " no existe en la base de datos."));

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
                .orElseThrow(() -> new EntityNotFoundException("El voluntario con ID " + id + " no existe en la base de datos."));

        PersonalInformationEntity personalInfo = volunteer.getPersonalInformation();
        PersonalInformationEntity newPersonalInfo = entity.getPersonalInformation();
        personalInfo.setPhoneNumber(newPersonalInfo.getPhoneNumber());
        personalInfo.setAddress(newPersonalInfo.getAddress());
        personalInfo.setBirthDate(newPersonalInfo.getBirthDate());

        validateAge(personalInfo.getBirthDate());
        personalInfo.setAge(calculateAge(personalInfo.getBirthDate()));

        VolunteeringInformationEntity volunteeringInfo = volunteer.getVolunteeringInformation();
        VolunteeringInformationEntity newVolunteeringInfo = entity.getVolunteeringInformation();
        newVolunteeringInfo.setVolunteerType(volunteeringInfo.getVolunteerType());
        volunteeringInfo.setVolunteerType(newVolunteeringInfo.getVolunteerType());
        volunteeringInfo.setVolunteeredHours(newVolunteeringInfo.getVolunteeredHours());

        volunteer.setEmergencyInformation(entity.getEmergencyInformation());

        return volunteerRepository.save(volunteer);
    }
}
