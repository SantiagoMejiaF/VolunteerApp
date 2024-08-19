package com.constructiveactivists.missionandactivitymanagementmodule.mappers;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.VolunteerGroupRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.VolunteerGroupEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VolunteerGroupMapper {

    VolunteerGroupEntity toDomain (VolunteerGroupRequest volunteerGroupEntity);
}
