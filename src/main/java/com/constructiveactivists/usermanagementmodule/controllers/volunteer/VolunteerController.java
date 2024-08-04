package com.constructiveactivists.usermanagementmodule.controllers.volunteer;

import com.constructiveactivists.usermanagementmodule.controllers.volunteer.configuration.VolunteerAPI;
import com.constructiveactivists.usermanagementmodule.controllers.volunteer.mappers.VolunteerMapper;
import com.constructiveactivists.usermanagementmodule.controllers.volunteer.request.VolunteerRequest;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.usermanagementmodule.services.VolunteerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.volunteer}")
@CrossOrigin(origins = "https://volunteer-app.online")
public class VolunteerController implements VolunteerAPI {

    private final VolunteerService volunteerService;

    private final VolunteerMapper volunteerMapper;

    @Override
    public List<VolunteerEntity> getAllVolunteers() {
        return volunteerService.getAllVolunteers();
    }

    @Override
    public ResponseEntity<VolunteerEntity> getVolunteerById(@PathVariable Integer id) {
        Optional<VolunteerEntity> volunteer = volunteerService.getVolunteerById(id);
        return volunteer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<VolunteerEntity> createVolunteer(@Valid @RequestBody VolunteerRequest volunteerRequest) {
        VolunteerEntity createdVolunteerEntity = volunteerService.saveVolunteer(volunteerMapper.toEntity(volunteerRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVolunteerEntity);
    }

    @Override
    public ResponseEntity<Void> deleteVolunteer(@PathVariable Integer id) {
        volunteerService.deleteVolunteer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
