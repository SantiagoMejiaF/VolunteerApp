package com.constructiveactivists.volunteermanagementmodule.mappers;

import com.constructiveactivists.volunteermanagementmodule.controllers.request.PersonalUpdateInformationRequest;
import com.constructiveactivists.volunteermanagementmodule.entities.PersonalInformationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonalUpdateInformationMapper {
    PersonalInformationEntity toEntity(PersonalUpdateInformationRequest personalUpdateInformationRequest);
}
