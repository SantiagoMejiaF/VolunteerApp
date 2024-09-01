package com.constructiveactivists.missionandactivitymodule.mappers.volunteergroup;

import com.constructiveactivists.missionandactivitymodule.controllers.request.volunteergroup.VolunteerGroupMembershipRequest;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupMembershipEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VolunteerGroupMembershipMapper {

    VolunteerGroupMembershipEntity toDomain(VolunteerGroupMembershipRequest volunteerGroupMembershipRequest);
}
