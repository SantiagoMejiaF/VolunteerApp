package com.constructiveactivists.dashboardsandreportsmodule.controllers.response;

import com.constructiveactivists.organizationmodule.entities.organization.enums.OrganizationTypeEnum;
import com.constructiveactivists.organizationmodule.entities.organization.enums.SectorTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardsOrganizationVolunteerResponse {

    private Integer organizationId;

    private String name;

    private String description;

    private OrganizationTypeEnum organizationType;

    private String photoUrl;

    private long authorizedVolunteersCount;

    private SectorTypeEnum sector;
}
