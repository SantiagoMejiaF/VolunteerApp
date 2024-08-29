package com.constructiveactivists.volunteermanagementmodule.mappers;

import com.constructiveactivists.volunteermanagementmodule.controllers.request.volunteergroup.VolunteerGroupMembershipRequest;
import com.constructiveactivists.volunteermanagementmodule.entities.volunteergroup.VolunteerGroupMembershipEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VolunteerGroupMembershipMapper {

    @Mapping(target = "group", ignore = true) // No se configura el grupo aquí
    @Mapping(target = "volunteerId", source = "volunteer") // Mapea el ID del voluntario
    VolunteerGroupMembershipEntity toDomain(VolunteerGroupMembershipRequest volunteerGroupMembershipRequest);

}
