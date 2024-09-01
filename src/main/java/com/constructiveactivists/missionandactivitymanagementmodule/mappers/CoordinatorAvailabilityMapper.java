package com.constructiveactivists.missionandactivitymanagementmodule.mappers;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.activity.CoordinatorAvailabilityRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.activitycoordinator.CoordinatorAvailabilityModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CoordinatorAvailabilityMapper {
    CoordinatorAvailabilityModel toDomain(CoordinatorAvailabilityRequest request);
}
