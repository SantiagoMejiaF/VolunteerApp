package com.constructiveactivists.volunteermodule.mappers.volunteer;

import com.constructiveactivists.volunteermodule.controllers.request.volunteer.EmergencyInformationRequest;
import com.constructiveactivists.volunteermodule.entities.volunteer.EmergencyInformationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmergencyInformationMapper {

    EmergencyInformationEntity toEntity(EmergencyInformationRequest emergencyInformationRequest);
}
