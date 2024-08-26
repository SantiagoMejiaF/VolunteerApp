package com.constructiveactivists.missionandactivitymanagementmodule.mappers;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.activity.ActivityCoordinatorRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityCoordinatorMapper {

    ActivityCoordinatorEntity toDomain(ActivityCoordinatorRequest activityCoordinatorRequest);
}
