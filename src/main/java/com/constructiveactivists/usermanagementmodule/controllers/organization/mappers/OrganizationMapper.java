package com.constructiveactivists.usermanagementmodule.controllers.organization.mappers;

import com.constructiveactivists.usermanagementmodule.controllers.organization.request.OrganizationRequest;
import com.constructiveactivists.usermanagementmodule.entities.organization.OrganizationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    OrganizationEntity toDomain (OrganizationRequest organizationRequest);
}
