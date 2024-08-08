package com.constructiveactivists.volunteermanagementmodule.controllers.mappers;

import com.constructiveactivists.volunteermanagementmodule.controllers.request.PersonalInformationRequest;
import com.constructiveactivists.volunteermanagementmodule.entities.PersonalInformationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonalInformationMapper {

    PersonalInformationEntity toEntity (PersonalInformationRequest personalInformationRequest);
}
