package com.constructiveactivists.postulationmanagementmodule.mapper;

import com.constructiveactivists.postulationmanagementmodule.controller.request.VolunteerOrganizationRequest;
import com.constructiveactivists.postulationmanagementmodule.entities.VolunteerOrganizationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VolunteerOrganizationMapper {
    VolunteerOrganizationEntity toEntity(VolunteerOrganizationRequest volunteerOrganizationRequest);
}
