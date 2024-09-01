package com.constructiveactivists.volunteermodule.mappers.volunteer;

import com.constructiveactivists.volunteermodule.controllers.request.volunteer.VolunteerRequest;
import com.constructiveactivists.volunteermodule.controllers.request.volunteer.VolunteerUpdateRequest;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {PersonalInformationMapper.class, VolunteeringInformationMapper.class, EmergencyInformationMapper.class})
public interface VolunteerMapper {

    VolunteerEntity toEntity(VolunteerRequest volunteerRequest);

    @Mapping(source = "personalUpdateInformationRequest", target = "personalInformation")
    VolunteerEntity toEntity(VolunteerUpdateRequest volunteerUpdateRequest);
}
