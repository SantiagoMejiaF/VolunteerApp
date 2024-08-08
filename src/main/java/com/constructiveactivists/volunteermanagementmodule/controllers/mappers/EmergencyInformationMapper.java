package com.constructiveactivists.volunteermanagementmodule.controllers.mappers;

import com.constructiveactivists.volunteermanagementmodule.controllers.request.EmergencyInformationRequest;
import com.constructiveactivists.volunteermanagementmodule.entities.EmergencyInformationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmergencyInformationMapper {

    EmergencyInformationEntity toEntity(EmergencyInformationRequest emergencyInformationRequest);
}
