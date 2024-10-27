package com.constructiveactivists.volunteermodule.controllers.response;

import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RankedOrganizationResponse {

    private OrganizationEntity organization;

    private int score;

    private String photoUrl;

    private long authorizedVolunteersCount;
}
