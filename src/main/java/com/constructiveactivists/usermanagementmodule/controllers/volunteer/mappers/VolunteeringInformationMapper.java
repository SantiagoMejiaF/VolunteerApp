package com.constructiveactivists.usermanagementmodule.controllers.volunteer.mappers;

import com.constructiveactivists.usermanagementmodule.controllers.volunteer.request.VolunteeringInformationRequest;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.VolunteeringInformationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VolunteeringInformationMapper {

    VolunteeringInformationEntity toEntity (VolunteeringInformationRequest volunteeringInformationRequest);
}
