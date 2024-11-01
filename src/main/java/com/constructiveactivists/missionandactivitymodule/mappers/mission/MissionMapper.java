package com.constructiveactivists.missionandactivitymodule.mappers.mission;

import com.constructiveactivists.missionandactivitymodule.controllers.request.mission.MissionRequest;
import com.constructiveactivists.missionandactivitymodule.controllers.request.mission.MissionUpdateRequest;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MissionMapper {

    MissionEntity toDomain (MissionRequest missionRequest);

    MissionEntity toEntity(MissionUpdateRequest missionUpdateRequest);
}
