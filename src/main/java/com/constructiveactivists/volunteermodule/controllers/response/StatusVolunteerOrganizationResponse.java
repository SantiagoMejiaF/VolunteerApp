package com.constructiveactivists.volunteermodule.controllers.response;

import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusVolunteerOrganizationResponse {
    private String fullName;
    private String email;
    private String identificationNumber;
    private OrganizationStatusEnum status;
    private Integer id;
}
