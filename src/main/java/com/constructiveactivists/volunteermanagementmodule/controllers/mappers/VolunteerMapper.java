package com.constructiveactivists.volunteermanagementmodule.controllers.mappers;

import com.constructiveactivists.volunteermanagementmodule.controllers.request.VolunteerRequest;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {PersonalInformationMapper.class, VolunteeringInformationMapper.class, EmergencyInformationMapper.class})
public interface VolunteerMapper {

    VolunteerEntity toEntity(VolunteerRequest volunteerRequest);
}
