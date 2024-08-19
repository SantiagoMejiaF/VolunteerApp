package com.constructiveactivists.volunteermanagementmodule.mappers;

import com.constructiveactivists.volunteermanagementmodule.controllers.request.VolunteeringInformationRequest;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteeringInformationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VolunteeringInformationMapper {

    VolunteeringInformationEntity toEntity (VolunteeringInformationRequest volunteeringInformationRequest);
}
