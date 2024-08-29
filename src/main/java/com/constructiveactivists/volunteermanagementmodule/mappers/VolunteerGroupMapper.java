package com.constructiveactivists.volunteermanagementmodule.mappers;

import com.constructiveactivists.volunteermanagementmodule.controllers.request.volunteergroup.VolunteerGroupRequest;
import com.constructiveactivists.volunteermanagementmodule.entities.volunteergroup.VolunteerGroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {VolunteerGroupMembershipMapper.class})
public interface VolunteerGroupMapper {

    @Mapping(target = "memberships", source = "volunteerIds")
    VolunteerGroupEntity toDomain(VolunteerGroupRequest volunteerGroupRequest);

}
