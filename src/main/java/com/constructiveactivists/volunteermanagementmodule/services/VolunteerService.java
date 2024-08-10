package com.constructiveactivists.volunteermanagementmodule.services;

import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.RoleType;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.*;
import com.constructiveactivists.volunteermanagementmodule.repositories.VolunteerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final UserService userService;

    public List<VolunteerEntity> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    public Optional<VolunteerEntity> getVolunteerById(Integer id) {
        return volunteerRepository.findById(id);
    }

    public VolunteerEntity saveVolunteer(VolunteerEntity volunteerEntity) {

        Optional<UserEntity> user = userService.getUserById(volunteerEntity.getUserId());

        if (user.isEmpty()) {
            throw new EntityNotFoundException("Usuario con ID " + volunteerEntity.getUserId() + " no existe.");
        }

        userService.updateUserRoleType(volunteerEntity.getUserId(), RoleType.ORGANIZACION);
        volunteerEntity.setStatus(StatusEnum.valueOf("ACTIVO"));
        volunteerEntity.getVolunteeringInformation().setVolunteerType(VolunteerType.valueOf("VOLUNTARIO"));
        volunteerEntity.getVolunteeringInformation().setVolunteeredHours(0);

        return volunteerRepository.save(volunteerEntity);
    }

    public void deleteVolunteer(Integer id) {
        VolunteerEntity volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voluntario no encontrado con id " + id));

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
}
