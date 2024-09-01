package com.constructiveactivists.volunteermodule.mappers.volunteerorganization;

import com.constructiveactivists.volunteermodule.controllers.request.volunteerorganization.VolunteerOrganizationRequest;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VolunteerOrganizationMapper {
    VolunteerOrganizationEntity toEntity(VolunteerOrganizationRequest volunteerOrganizationRequest);
}
