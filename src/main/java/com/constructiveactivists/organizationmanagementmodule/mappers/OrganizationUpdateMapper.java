package com.constructiveactivists.organizationmanagementmodule.mappers;

import com.constructiveactivists.organizationmanagementmodule.controllers.request.OrganizationUpdateRequest;
import com.constructiveactivists.organizationmanagementmodule.entities.OrganizationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationUpdateMapper {
    OrganizationEntity toDomain(OrganizationUpdateRequest organizationUpdateRequest);
}
