package com.constructiveactivists.missionandactivitymanagementmodule.mappers;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.MissionRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.MissionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MissionMapper {

    MissionEntity toDomain (MissionRequest missionRequest);
}
