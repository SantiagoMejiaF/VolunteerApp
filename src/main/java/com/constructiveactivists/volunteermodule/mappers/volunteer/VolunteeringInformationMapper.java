package com.constructiveactivists.volunteermodule.mappers.volunteer;

import com.constructiveactivists.volunteermodule.controllers.request.volunteer.VolunteeringInformationRequest;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteeringInformationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VolunteeringInformationMapper {

    VolunteeringInformationEntity toEntity (VolunteeringInformationRequest volunteeringInformationRequest);
}
