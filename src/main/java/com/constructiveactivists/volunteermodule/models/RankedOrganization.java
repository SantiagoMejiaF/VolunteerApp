package com.constructiveactivists.volunteermodule.models;

import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RankedOrganization {

    private OrganizationEntity organization;

    private int score;

    private String photoUrl;

    private long authorizedVolunteersCount;
}
