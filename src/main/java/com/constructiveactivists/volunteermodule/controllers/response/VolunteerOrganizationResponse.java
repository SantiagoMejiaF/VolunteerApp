package com.constructiveactivists.volunteermodule.controllers.response;

import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.EmergencyInformationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.PersonalInformationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteeringInformationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerOrganizationResponse {

    private Integer id;
    private Integer volunteerId;
    private Integer organizationId;

    private OrganizationStatusEnum status;
    private LocalDate registrationDate;

    private Integer hoursDone;
    private Integer hoursCertified;
    private Integer monthlyHours;

    private Integer userId;
    private VisibilityEnum visibility;
    private PersonalInformationEntity personalInformation;
    private VolunteeringInformationEntity volunteeringInformation;
    private EmergencyInformationEntity emergencyInformation;

}
