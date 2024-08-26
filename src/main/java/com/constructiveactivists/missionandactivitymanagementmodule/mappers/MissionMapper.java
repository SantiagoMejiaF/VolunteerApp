package com.constructiveactivists.missionandactivitymanagementmodule.mappers;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.mission.MissionRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.mission.MissionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MissionMapper {

    MissionEntity toDomain (MissionRequest missionRequest);
}
