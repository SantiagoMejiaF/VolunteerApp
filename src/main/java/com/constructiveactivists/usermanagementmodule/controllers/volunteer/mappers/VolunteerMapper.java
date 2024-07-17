package com.constructiveactivists.usermanagementmodule.controllers.volunteer.mappers;

import com.constructiveactivists.usermanagementmodule.controllers.volunteer.request.VolunteerRequest;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.VolunteerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {PersonalInformationMapper.class, VolunteeringInformationMapper.class, EmergencyInformationMapper.class})
public interface VolunteerMapper {

    VolunteerEntity toEntity(VolunteerRequest volunteerRequest);
}
