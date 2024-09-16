package com.constructiveactivists.missionandactivitymodule.mappers.activity;

import com.constructiveactivists.missionandactivitymodule.controllers.request.activity.ReviewRequest;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ReviewEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewEntity toDomain (ReviewRequest reviewRequest);
}
