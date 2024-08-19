package com.constructiveactivists.organizationmanagementmodule.mappers;

import com.constructiveactivists.organizationmanagementmodule.controllers.request.OrganizationRequest;
import com.constructiveactivists.organizationmanagementmodule.entities.OrganizationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    OrganizationEntity toDomain (OrganizationRequest organizationRequest);
}
