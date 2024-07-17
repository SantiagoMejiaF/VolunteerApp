package com.constructiveactivists.usermanagementmodule.controllers.volunteer.mappers;

import com.constructiveactivists.usermanagementmodule.controllers.volunteer.request.PersonalInformationRequest;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.PersonalInformationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonalInformationMapper {

    PersonalInformationEntity toEntity (PersonalInformationRequest personalInformationRequest);
}
