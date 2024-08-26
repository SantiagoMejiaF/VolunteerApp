package com.constructiveactivists.missionandactivitymanagementmodule.mappers;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.activity.ActivityRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.activity.ActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {ActivityCoordinatorMapper.class, PersonalDataCommunityLeaderMapper.class})
public interface ActivityMapper {

    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    ActivityEntity toDomain (ActivityRequest activityRequest);
}
