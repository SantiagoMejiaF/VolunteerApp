package com.constructiveactivists.volunteermodule.mappers.volunteer;

import com.constructiveactivists.volunteermodule.controllers.request.volunteer.VolunteerUpdateRequest;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {PersonalUpdateInformationMapper.class, VolunteeringInformationMapper.class, EmergencyInformationMapper.class})
public interface VolunteerUpdateMapper {

    @Mapping(source = "personalUpdateInformationRequest", target = "personalInformation")
    VolunteerEntity toEntity(VolunteerUpdateRequest volunteerUpdateRequest);
}
