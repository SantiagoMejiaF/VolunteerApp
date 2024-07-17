package com.constructiveactivists.usermanagementmodule.services;

import com.constructiveactivists.usermanagementmodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.usermanagementmodule.repositories.VolunteerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;

    public List<VolunteerEntity> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    public Optional<VolunteerEntity> getVolunteerById(Integer id) {
        return volunteerRepository.findById(id);
    }

    public VolunteerEntity saveVolunteer(VolunteerEntity volunteerEntity) {
        if (volunteerEntity.getVolunteeringInformation() != null) {
            volunteerEntity.getVolunteeringInformation().setVolunteeredHours(0);
            volunteerEntity.getVolunteeringInformation().setRegistrationDate(LocalDate.now());
        }
        return volunteerRepository.save(volunteerEntity);
    }

    public void deleteVolunteer(Integer id) {
        volunteerRepository.deleteById(id);
    }
}
