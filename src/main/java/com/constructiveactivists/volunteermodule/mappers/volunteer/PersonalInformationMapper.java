package com.constructiveactivists.volunteermodule.mappers.volunteer;

import com.constructiveactivists.volunteermodule.controllers.request.volunteer.PersonalInformationRequest;
import com.constructiveactivists.volunteermodule.entities.volunteer.PersonalInformationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonalInformationMapper {

    PersonalInformationEntity toEntity (PersonalInformationRequest personalInformationRequest);
}
