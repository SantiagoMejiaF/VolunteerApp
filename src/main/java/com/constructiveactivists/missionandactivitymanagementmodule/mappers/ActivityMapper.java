package com.constructiveactivists.missionandactivitymanagementmodule.mappers;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.ActivityRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.ActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    ActivityEntity toDomain (ActivityRequest activityRequest);
}
