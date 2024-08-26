package com.constructiveactivists.missionandactivitymanagementmodule.mappers;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.volunteergroup.VolunteerGroupRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.volunteergroup.VolunteerGroupEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VolunteerGroupMapper {

    VolunteerGroupEntity toDomain (VolunteerGroupRequest volunteerGroupEntity);
}
