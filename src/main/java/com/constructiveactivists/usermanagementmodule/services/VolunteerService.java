package com.constructiveactivists.usermanagementmodule.services;

import com.constructiveactivists.usermanagementmodule.entities.user.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.SkillEnum;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.StatusEnum;
import com.constructiveactivists.usermanagementmodule.repositories.UserRepository;
import com.constructiveactivists.usermanagementmodule.repositories.VolunteerRepository;
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

    private final UserRepository userRepository;

    public List<VolunteerEntity> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    public Optional<VolunteerEntity> getVolunteerById(Integer id) {
        return volunteerRepository.findById(id);
    }

    public VolunteerEntity saveVolunteer(VolunteerEntity volunteerEntity) {
        Optional<UserEntity> user = userRepository.findById(volunteerEntity.getUserId());
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Usuario con ID " + volunteerEntity.getUserId() + " no existe.");
        }

        userRepository.updateRole(volunteerEntity.getUserId(), 2);

        volunteerEntity.setStatus(StatusEnum.valueOf("ACTIVO"));
        volunteerEntity.setGroupLeader(false);
        volunteerEntity.getVolunteeringInformation().setVolunteeredHours(0);
        return volunteerRepository.save(volunteerEntity);

    }

    public void deleteVolunteer(Integer id) {
        VolunteerEntity volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Volunteer not found with id " + id));
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
}
