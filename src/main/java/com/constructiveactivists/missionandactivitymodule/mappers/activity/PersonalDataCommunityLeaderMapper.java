package com.constructiveactivists.missionandactivitymodule.mappers.activity;

import com.constructiveactivists.missionandactivitymodule.controllers.request.activity.PersonalDataCommunityLeaderRequest;
import com.constructiveactivists.missionandactivitymodule.entities.activity.PersonalDataCommunityLeaderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonalDataCommunityLeaderMapper {

    PersonalDataCommunityLeaderEntity toDomain(PersonalDataCommunityLeaderRequest personalDataCommunityLeaderRequest);
}
