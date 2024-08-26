package com.constructiveactivists.missionandactivitymanagementmodule.mappers;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.request.activity.PersonalDataCommunityLeaderRequest;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.activity.PersonalDataCommunityLeaderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonalDataCommunityLeaderMapper {

    PersonalDataCommunityLeaderEntity toDomain(PersonalDataCommunityLeaderRequest personalDataCommunityLeaderRequest);
}
