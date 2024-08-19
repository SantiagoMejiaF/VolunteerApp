package com.constructiveactivists.volunteermanagementmodule.mappers;

import com.constructiveactivists.volunteermanagementmodule.controllers.request.VolunteerRequest;
import com.constructiveactivists.volunteermanagementmodule.controllers.request.VolunteerUpdateRequest;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {PersonalInformationMapper.class, VolunteeringInformationMapper.class, EmergencyInformationMapper.class})
public interface VolunteerMapper {

    VolunteerEntity toEntity(VolunteerRequest volunteerRequest);

    @Mapping(source = "personalUpdateInformationRequest", target = "personalInformation")
    VolunteerEntity toEntity(VolunteerUpdateRequest volunteerUpdateRequest);
}
