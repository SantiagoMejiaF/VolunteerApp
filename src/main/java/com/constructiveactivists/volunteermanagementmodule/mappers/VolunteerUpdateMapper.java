package com.constructiveactivists.volunteermanagementmodule.mappers;

import com.constructiveactivists.volunteermanagementmodule.controllers.request.VolunteerUpdateRequest;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {PersonalUpdateInformationMapper.class, VolunteeringInformationMapper.class, EmergencyInformationMapper.class})
public interface VolunteerUpdateMapper {

    @Mapping(source = "personalUpdateInformationRequest", target = "personalInformation")
    VolunteerEntity toEntity(VolunteerUpdateRequest volunteerUpdateRequest);
}
