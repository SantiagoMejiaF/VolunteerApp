package com.constructiveactivists.organizationmodule.mappers.activitycoordinator;

import com.constructiveactivists.organizationmodule.controllers.request.activitycoordinator.ActivityCoordinatorRequest;
import com.constructiveactivists.organizationmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityCoordinatorMapper {

    ActivityCoordinatorEntity toDomain(ActivityCoordinatorRequest activityCoordinatorRequest);
}
