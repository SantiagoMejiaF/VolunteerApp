package com.constructiveactivists.missionandactivitymodule.mappers.activity;

import com.constructiveactivists.missionandactivitymodule.controllers.request.activity.ActivityRequest;
import com.constructiveactivists.missionandactivitymodule.controllers.request.activity.ActivityUpdateRequest;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.organizationmodule.mappers.activitycoordinator.ActivityCoordinatorMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {ActivityCoordinatorMapper.class, PersonalDataCommunityLeaderMapper.class})
public interface ActivityMapper {

    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    ActivityEntity toDomain (ActivityRequest activityRequest);

    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    ActivityEntity toEntity (ActivityUpdateRequest activityRequest);
}
