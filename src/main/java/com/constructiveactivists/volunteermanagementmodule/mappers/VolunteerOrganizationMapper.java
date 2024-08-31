package com.constructiveactivists.volunteermanagementmodule.mappers;

import com.constructiveactivists.volunteermanagementmodule.controllers.request.VolunteerOrganizationRequest;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerOrganizationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VolunteerOrganizationMapper {
    VolunteerOrganizationEntity toEntity(VolunteerOrganizationRequest volunteerOrganizationRequest);
}
