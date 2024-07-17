package com.constructiveactivists.usermanagementmodule.controllers.volunteer.mappers;

import com.constructiveactivists.usermanagementmodule.controllers.volunteer.request.EmergencyInformationRequest;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.EmergencyInformationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmergencyInformationMapper {

    EmergencyInformationEntity toEntity(EmergencyInformationRequest emergencyInformationRequest);
}
