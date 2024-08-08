package com.constructiveactivists.facademodule;

import com.constructiveactivists.authenticationmodule.controllers.AuthenticationController;
import com.constructiveactivists.authenticationmodule.controllers.request.AuthenticationRequest;
import com.constructiveactivists.organizationmanagementmodule.controllers.OrganizationController;
import com.constructiveactivists.organizationmanagementmodule.controllers.request.OrganizationRequest;
import com.constructiveactivists.organizationmanagementmodule.entities.OrganizationEntity;
import com.constructiveactivists.usermanagementmodule.controllers.UserController;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.volunteermanagementmodule.controllers.VolunteerController;
import com.constructiveactivists.volunteermanagementmodule.controllers.request.VolunteerRequest;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.InterestEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.RelationshipEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.SkillEnum;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.facade}")
public class FacadeController {

    private final UserController userController;
    private final VolunteerController volunteerController;
    private final AuthenticationController authenticationController;
    private final OrganizationController organizationController;

    // Métodos de VolunteerController
    @GetMapping("/volunteers")
    public List<VolunteerEntity> getAllVolunteers() {
        return volunteerController.getAllVolunteers();
    }

    @GetMapping("/volunteers/{id}")
    public ResponseEntity<VolunteerEntity> getVolunteerById(@PathVariable Integer id) {
        return volunteerController.getVolunteerById(id);
    }

    @PostMapping("/volunteers")
    public ResponseEntity<VolunteerEntity> createVolunteer(@Valid @RequestBody VolunteerRequest volunteerRequest) {
        return volunteerController.createVolunteer(volunteerRequest);
    }

    @DeleteMapping("/volunteers/{id}")
    public ResponseEntity<Void> deleteVolunteer(@PathVariable Integer id) {
        return volunteerController.deleteVolunteer(id);
    }

    @GetMapping("/volunteers/interests")
    public ResponseEntity<List<InterestEnum>> getAllInterests() {
        return volunteerController.getAllInterests();
    }

    @GetMapping("/volunteers/skills")
    public ResponseEntity<List<SkillEnum>> getAllSkills() {
        return volunteerController.getAllSkills();
    }

    @GetMapping("/volunteers/availabilities")
    public ResponseEntity<List<AvailabilityEnum>> getAllAvailabilities() {
        return volunteerController.getAllAvailabilities();
    }

    @GetMapping("/volunteers/relationships")
    public ResponseEntity<List<RelationshipEnum>> getAllRelationships() {
        return volunteerController.getAllRelationships();
    }

    // Métodos de AuthenticationController
    @PostMapping("/authenticate/google")
    public ResponseEntity<UserEntity> authenticationByGoogle(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationController.authenticationByGoogle(authenticationRequest);
    }

    // Métodos de OrganizationController
    @GetMapping("/organizations")
    public ResponseEntity<List<OrganizationEntity>> getAllOrganizations() {
        return organizationController.getAllOrganizations();
    }

    @GetMapping("/organizations/{id}")
    public ResponseEntity<OrganizationEntity> getOrganizationById(@PathVariable Integer id) {
        return organizationController.getOrganizationById(id);
    }

    @PostMapping("/organizations")
    public ResponseEntity<OrganizationEntity> createOrganization(@Valid @RequestBody OrganizationRequest organizationRequest) {
        return organizationController.createOrganization(organizationRequest);
    }

    @PutMapping("/organizations/{id}")
    public ResponseEntity<OrganizationEntity> updateOrganization(@PathVariable Integer id, @RequestBody OrganizationRequest organizationRequest) {
        return organizationController.updateOrganization(id, organizationRequest);
    }

    @DeleteMapping("/organizations/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Integer id) {
        return organizationController.deleteOrganization(id);
    }


}