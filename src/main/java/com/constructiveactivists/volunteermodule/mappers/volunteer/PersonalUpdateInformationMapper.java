package com.constructiveactivists.volunteermodule.mappers.volunteer;

import com.constructiveactivists.volunteermodule.controllers.request.volunteer.PersonalUpdateInformationRequest;
import com.constructiveactivists.volunteermodule.entities.volunteer.PersonalInformationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonalUpdateInformationMapper {

    PersonalInformationEntity toEntity(PersonalUpdateInformationRequest personalUpdateInformationRequest);
}
