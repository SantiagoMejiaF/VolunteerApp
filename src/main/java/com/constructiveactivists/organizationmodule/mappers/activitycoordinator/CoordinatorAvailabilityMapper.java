package com.constructiveactivists.organizationmodule.mappers.activitycoordinator;

import com.constructiveactivists.organizationmodule.controllers.request.activitycoordinator.CoordinatorAvailabilityRequest;
import com.constructiveactivists.organizationmodule.entities.activitycoordinator.CoordinatorAvailabilityModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CoordinatorAvailabilityMapper {
    CoordinatorAvailabilityModel toDomain(CoordinatorAvailabilityRequest request);
}
