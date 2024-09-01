package com.constructiveactivists.missionandactivitymodule.mappers.volunteergroup;

import com.constructiveactivists.missionandactivitymodule.controllers.request.volunteergroup.VolunteerGroupRequest;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {VolunteerGroupMembershipMapper.class})
public interface VolunteerGroupMapper {

    @Mapping(target = "memberships", source = "volunteerIds")
    VolunteerGroupEntity toDomain(VolunteerGroupRequest volunteerGroupRequest);

}
